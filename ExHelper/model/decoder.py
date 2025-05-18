import logging
import os
import json
from abc import ABC, abstractmethod
from typing import List, Dict
import tiktoken
from openai import OpenAI


class DecoderBase(ABC):
    def __init__(self, name: str, logger, batch_size: int, temperature: float, max_tokens: int):
        self.name = name
        self.logger = logger
        self.batch_size = batch_size
        self.temperature = temperature
        self.max_tokens = max_tokens

    @abstractmethod
    def codegen(self, message: str, num_samples: int = 1) -> List[Dict]:
        pass

    @abstractmethod
    def is_direct_completion(self) -> bool:
        pass

    def __repr__(self):
        return self.name


class OpenAIChatDecoder(DecoderBase):
    def __init__(self, name: str, logger, batch_size: int, temperature: float, max_tokens: int, client):
        super().__init__(name, logger, batch_size, temperature, max_tokens)
        self.client = client

    def codegen(self, message: str, num_samples: int = 1) -> List[Dict]:
        # 计算字符串的 token 数量
        def num_tokens_from_string(string: str, encoding_name: str) -> int:
            try:
                encoding = tiktoken.encoding_for_model(encoding_name)
                num_tokens = len(encoding.encode(string))
                return num_tokens
            except KeyError:
                default_encoding_name = "cl100k_base"
                encoding = tiktoken.get_encoding(default_encoding_name)
                num_tokens = len(encoding.encode(string))
                return num_tokens

        # 切片消息
        def slice_message(message: str, max_tokens: int) -> List[str]:
            try:
                encoding = tiktoken.encoding_for_model(self.name)
            except KeyError:
                default_encoding_name = "cl100k_base"
                encoding = tiktoken.get_encoding(default_encoding_name)
            tokens = encoding.encode(message)
            slices = []
            start = 0
            while start < len(tokens):
                end = min(start + max_tokens, len(tokens))
                slices.append(encoding.decode(tokens[start:end]))
                start = end
            return slices

        max_message_tokens = self.max_tokens - 50  # 预留 50 个 token
        message_slices = slice_message(message, max_message_tokens)

        all_responses = [{"response": "", "usage": {"prompt_tokens": 0, "completion_tokens": 0, "total_tokens": 0}} for _ in
                         range(num_samples)]

        for slice_msg in message_slices:
            try:
                response = self.client.chat.completions.create(
                    model=self.name,
                    messages=[{"role": "user", "content": slice_msg}],
                    temperature=self.temperature,
                    max_tokens=self.max_tokens,
                    n=num_samples
                )
                for choice in response.choices:
                    index = choice.index
                    all_responses[index]["response"] += choice.message.content
                    all_responses[index]["usage"]["prompt_tokens"] += response.usage.prompt_tokens
                    all_responses[index]["usage"]["completion_tokens"] += response.usage.completion_tokens
                    all_responses[index]["usage"]["total_tokens"] += response.usage.total_tokens
            except Exception as e:
                self.logger.error(f"OpenAI API error: {str(e)}")
                return []

        return all_responses

    def is_direct_completion(self) -> bool:
        return False




