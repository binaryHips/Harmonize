from connexion import FlaskApp #pip ->  connexion, connexion[flask] et connexion[uvicorn]
from pathlib import Path

# launches the app from this script and sets up the test client in the next funcion
testClient = True
base_kwargs = {}

# https://connexion.readthedocs.io/en/latest/quickstart.html
def test_client(client):
    # https://www.starlette.io/responses/
    response = client.post(
      "/authenticate",
      params = {"username" : "Tom", "password_hash" : "134"}
    )
    
    print(response.content.decode('UTF-8'))

    # test avec un bon mot de passe
    response = client.post(
      "/authenticate",
      params = {"username" : "Kai", "password_hash" : "5678"}
    )
    
    print(response.content.decode('UTF-8'))



app = FlaskApp(__name__)

app.add_api("openapi.yaml")



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

# seulement en développement
if __name__ == "__main__":


  if testClient:
    with app.test_client(**base_kwargs) as client:
      print(f"{bcolors.BOLD + bcolors.OKCYAN}\nRunning the test client...\n{bcolors.ENDC}")
      test_client(client)

  else:
    # unsure if we should do that here insteade of running uvicorn outside of the script
    print(f"{bcolors.BOLD + bcolors.OKCYAN}\nRunning the server...\n{bcolors.ENDC}")
    app.run(f"{Path(__file__).stem}:app")