import subprocess
from PIL import Image
from pathlib import Path
import shutil

def recolor_image(image, savePath, template_letter = None, rotation = None):

    savePath = savePath # + "test.png"
    executable_path = "./backend/harmonize_windows.exe"

    if template_letter == None:
        template_letter = "ND"
    if rotation is None:
        rotation = "ND"
    saveImg(image, savePath + "original.png")

    process_result = subprocess.run(
        [
            executable_path,
            Path(savePath + "original.png").absolute(),
            "harmonized",
            str(template_letter),
            str(rotation)
        ], capture_output=True, text=True, cwd=savePath)
    print(process_result.stdout)

    recolored = Image.open(savePath + "harmonized.png")

    return recolored

def saveImg(img, path):
    try: 
        img.save(path)
    except FileNotFoundError:
        Path(path).parent.mkdir(parents=True, exist_ok=True)
        # don't copy if doesnt exist.
        img.save(path)

if __name__ == "__main__":
    test = Image.open("./backend/testHarmo.PNG")
    recolor_image(test, "./backend/images/test/", "i", 10)