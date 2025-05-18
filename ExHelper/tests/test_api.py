import pytest
from ExHelper.api.app import app
import logging

# 降低日志级别，减少干扰
logging.getLogger("sentence_transformers").setLevel(logging.WARNING)

@pytest.fixture
def client():
    from fastapi.testclient import TestClient
    with TestClient(app) as client:
        yield client

def test_health_check(client):
    response = client.get('/health')
    assert response.status_code == 200
    assert response.json() == {'status': 'OK'}
    print("Health Check Passed")

def test_literature_chat(client):
    response = client.post('/chat/literature_analyst', json={
        'query': '帮我分析Transformer架构',
        'context': '这是一个关于深度学习的讨论',
        'results': ['文档1', '文档2', '文档3']
    })
    assert response.status_code == 200
    assert "response" in response.json()
    reply = response.json()["response"]
    print(f"Literature Chat Response: {reply}")
    assert "Transformer" in reply  # 根据预期内容调整断言

def test_paper_chat(client):
    response = client.post('/chat/paper_analyst', json={
        'query': '给我和机器学习相关的文章',
        'context': '这是一个关于机器学习的讨论',
        'results': ['文档A', '文档B', '文档C']
    })
    assert response.status_code == 200
    assert "response" in response.json()
    reply = response.json()["response"]
    print(f"Paper Chat Response: {reply}")
    assert "机器学习" in reply  # 根据预期内容调整断言

def test_invalid_role(client):
    response = client.post('/chat/invalid_role', json={'query': 'test'})
    assert response.status_code == 404
    assert 'Role invalid_role not found' in response.json()['detail']
    print("Invalid Role Test Passed")