import yaml
from pathlib import Path
from typing import Dict, Any

def load_config(config_path: str) -> Dict[str, Any]:
    """加载YAML配置文件"""
    with open(config_path, 'r', encoding='utf-8') as f:
        config = yaml.safe_load(f)
    return resolve_env_vars(config)

def resolve_env_vars(config: Dict) -> Dict:
    """解析环境变量占位符"""
    import os
    for k, v in config.items():
        if isinstance(v, str) and v.startswith("${") and v.endswith("}"):
            env_var = v[2:-1]
            config[k] = os.getenv(env_var)
    return config