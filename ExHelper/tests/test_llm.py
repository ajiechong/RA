import pytest
import requests
from ExHelper.utils.config_loader import load_config
from ExHelper.model.model_factory import ModelFactory, get_model

def test_llm_api_response():
    model = get_model("test")
    message = "Your test message here,tell me about the latest research in AI."
    response = model.codegen(message)
    if response:
        # 直接从列表中获取回复内容
        reply = response[0]["response"]
        print(f"API 回复: {reply}")
    else:
        print("未收到回复。")

if __name__ == "__main__":
    test_llm_api_response()