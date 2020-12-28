import main


def test_index():
    main.app.testing = True
    client = main.app.test_client()

    r = client.get('/')
    assert r.status_code == 400
    assert 'Hello World' in r.data.decode('utf-8')