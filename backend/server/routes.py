
import db

# routes

def authenticate(username: str, password_hash: str):
    if db.validateUser(username, password_hash):

        userToken = db.createNewToken(username)

        return userToken, 200
    else:
        return "Wrong Password", 510
    
def createAccount(username: str, password_hash: str):
    if db.addUser(username,password_hash):
        
        userToken = db.createNewToken(username)

        return userToken, 200
    else:
        return "User already exists", 510