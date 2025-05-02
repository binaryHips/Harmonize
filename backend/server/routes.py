

database = {
    "Tom" : "1234",
    "Kai" : "5678"
}
def authenticate(username: str, password_hash: str):
    if username in database.keys() and database[username] == password_hash:
        return "Bienvenue " + username
    else:
        return "Mauvais mot de passe : " + password_hash