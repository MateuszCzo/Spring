FROM ubuntu:20.04

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y \
    curl \
    python3 \
    python3-pip \
    && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://ollama.com/install.sh | sh

RUN ollama serve

RUN ollama pull llama3.1 \
    && ollama pull mxbai-embed-large

RUN pip3 install --no-cache-dir -r requirements.txt

EXPOSE 5000

CMD ["python3", "main.py"]
