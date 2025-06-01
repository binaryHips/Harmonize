from connexion import FlaskApp #pip ->  connexion, connexion[flask] et connexion[uvicorn]
from pathlib import Path
from connexion.datastructures import MediaTypeDict
from connexion.validators import (
    DefaultsJSONRequestBodyValidator,
    FormDataValidator,
    MultiPartFormDataValidator,
)

#in caseit crashes https://stackoverflow.com/questions/8688949/how-to-close-tcp-and-udp-ports-via-windows-command-line
# launches the app from this script and sets up the test client in the next funcion
testClient = False
ip_adress = "192.168.1.70" #ip de l'appareil dans la box.
port = 3000 #pas oublier d'ouvrir le port dans le pare feu ET de le forward
            # dans la box pour pouvoir y accéder par l'ipv4 statique

# https://connexion.readthedocs.io/en/latest/quickstart.html
def test_client(client):
    # https://www.starlette.io/responses/
    response = client.get(
      "/authenticate",
      params = {"username" : "Tom", "password_hash" : "134"}
    )
    
    print(response.content.decode('UTF-8'))

    # test avec un bon mot de passe
    response = client.get(
      "/authenticate",
      params = {"username" : "Kai", "password_hash" : "5678"}
    )
    
    print(response.content.decode('UTF-8'))


validator_map = {
    "body": MediaTypeDict(
        {
            "*/*json": DefaultsJSONRequestBodyValidator,
            "application/x-www-form-urlencoded": FormDataValidator,
            "multipart/form-data": MultiPartFormDataValidator,
        }
    ),
}



app = FlaskApp(__name__, validator_map=validator_map)

app.add_api("openapi.yaml", validator_map=validator_map)



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
    with app.test_client() as client:
      print(f"{bcolors.BOLD + bcolors.OKCYAN}\nRunning the test client...\n{bcolors.ENDC}")
      test_client(client)

  else:
    # unsure if we should do that here insteade of running uvicorn outside of the script
    print(f"{bcolors.BOLD + bcolors.OKCYAN}\nRunning the server...\n{bcolors.ENDC}")
    app.run(f"{Path(__file__).stem}:app", port=port, host = ip_adress)