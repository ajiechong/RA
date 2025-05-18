import logging
from openai import OpenAI
import os
import json
from ExHelper.model.decoder import OpenAIChatDecoder


class ModelFactory:
    def __init__(self, config: dict, logger=None):
        self.config = config
        self.logger = logger or logging.getLogger(__name__)

    def create_model(self, backend: str) -> OpenAIChatDecoder:
        if backend == "openai":
            api_config = self.config["openai"]
            client = OpenAI(
                api_key=api_config["api_key"],
                base_url=api_config["base_url"]
            )
            return OpenAIChatDecoder(
                name=api_config["default_model"],
                logger=self.logger,
                batch_size=api_config["batch_size"],
                temperature=api_config["temperature"],
                max_tokens=api_config["max_tokens"],
                client=client
            )
        else:
            raise ValueError(f"Unsupported backend: {backend}")


def get_model(logger_name):
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger(logger_name)
    config_file = 'config/llm_config.json'
    config_file = config_file.replace('/', os.path.sep)
    with open(config_file, "r", encoding="utf-8") as f:
        config = json.load(f)
    factory = ModelFactory(config=config, logger=logger)
    backend = "openai"
    model = factory.create_model(backend)
    if model:
        print("Getting the model")
    return model