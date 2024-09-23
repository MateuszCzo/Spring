Commands to run chatbot:

apt-get update
apt-get install curl
apt-get install python3
apt-get install python3-pip
apt install python3-venv

curl -fsSL https://ollama.com/install.sh | sh

ollama serve
ollama pull mxbai-embed-large

python3 -m venv chatbot-env
source chatbot-env/bin/activate (run "deactivate" command to leave)

pip3 install --no-cache-dir -r requirements.txt

python3 main.py
