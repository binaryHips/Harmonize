
import db
import PIL
import harmonize as hrm
from connexion import request
# routes

def authenticate(username: str, password_hash: str):
    if db.validateUser(username, password_hash):

        userToken = db.createNewToken(username)

        return userToken, 200
    else:
        return "Wrong Password", 510
    
def validateToken(token: str):
    return db.validateToken(str(token)) # TODO maybe do a bit more in this function?
    
def createAccount(username: str, password_hash: str):
    if db.addUser(username,password_hash):
        
        userToken = db.createNewToken(username)

        return userToken, 200
    else:
        return "User already exists", 510


userImagesPath = "./backend/"
async def harmonize(image,token:str, template:str = None, angle:float = None, **kwargs):

    return
    user = db._getUserFromToken(token)
    if not user : return "Invalid token", 510
    pilImage = PIL.Image.open(file) # image is a werkzeug.FileStorage. But it converts ! that's why I like python

    path = userImagesPath + user + "/"
    resImage:PIL.Image = hrm.recolor_image(pilImage, template, angle)

    return {
        'image': resImage # TODO https://stackoverflow.com/questions/67981810/python-post-request-using-pil-image
    }, 200
