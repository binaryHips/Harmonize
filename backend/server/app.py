from connexion import FlaskApp #pip ->  connexion, connexion[flask] et connexion[uvicorn]
from pathlib import Path

# launches the app from this script and sets up the text client in the next funcion
testClient = True
base_kwargs = {}

# https://connexion.readthedocs.io/en/latest/quickstart.html
def test_client(client):

    response = client.post("/greeting/joe")
    print("le status code est ", response.status_code)

app = FlaskApp(__name__)

app.add_api("openapi.yaml")



# seulement en développement
if __name__ == "__main__":

    class bcolors: # ansi colors, just for pretty printing
        HEADER = '\033[95m'
        OKBLUE = '\033[94m'
        OKCYAN = '\033[96m'
        OKGREEN = '\033[92m'
        WARNING = '\033[93m'
        FAIL = '\033[91m'
        ENDC = '\033[0m'
        BOLD = '\033[1m'
        UNDERLINE = '\033[4m'

    if testClient:
        with app.test_client(**base_kwargs) as client:
            print(f"{bcolors.BOLD + bcolors.OKCYAN}\nRunning the test client")
            test_client(client)

else:
    app.run(f"{Path(__file__).stem}:app")