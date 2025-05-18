import os
from utils.config_loader import load_config

# 加载 LLM 配置
LLM_CONFIG = load_config(os.path.join('config', 'llm_config.yaml'))

# 加载所有角色配置，并合并 LLM 参数
ROLE_CONFIGS = {
    'literature': {
        'role_config': load_config(os.path.join('config', 'roles', 'literature.yaml')),
        'llm_params': LLM_CONFIG['default'],  # 默认使用 default 配置
        'tool_config': load_config(os.path.join('config', 'roles', 'literature.yaml'))['tool_config']
    },
    'paper_analyst': {
        'role_config': load_config(os.path.join('config', 'roles', 'paper_analyst.yaml')),
        'llm_params': LLM_CONFIG['default'],
        'tool_config': load_config(os.path.join('config', 'roles', 'paper_analyst.yaml'))['tool_config']
    }
}