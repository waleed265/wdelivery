import main
import unittest

class NamesTesCase(unittest.TestCase):
    def test_index(self):
        
        main.app.testing = True 
        client = main.app.test_client()

        r = client.get('/')
        #assert r.status_code == 200, "status code should be '200'"
        assert 'Hello' in r.data.decode('utf-8')

        #self.assertTrue('teaher'.islower())

    def test_index1(self):
        print("Welcome to CICD")
        
        main.app.testing = True 
        client = main.app.test_client()

        r = client.get('/')
        assert r.status_code == 200, "status code should be '200'"
        #assert 'Hello' in r.data.decode('utf-8')

        #self.assertTrue('teaher'.islower())

if __name__=='__main__':
    unittest.main()