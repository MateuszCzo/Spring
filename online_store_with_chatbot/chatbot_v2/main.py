import torch
import ollama
from flask import Flask, request, jsonify
import csv
import os
from dotenv import load_dotenv
from openai import OpenAI


load_dotenv()
openai_api_key = os.getenv('OPENAI_API_KEY')
project_id = os.getenv('PROJECT_ID')


client = OpenAI(
    api_key = openai_api_key,
    project = project_id
)


def open_file(filepath):
    # with open(filepath, 'r', encoding='utf-8') as infile:
    #    return infile.readlines()
    content = []
    with open(filepath, 'r', encoding='utf-8') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            content.append(', '.join(row))
    return content


def generate_embeddings(texts, model_name):
    embeddings = []
    for text in texts:
        response = ollama.embeddings(model=model_name, prompt=text)
        embeddings.append(response["embedding"])
    return torch.tensor(embeddings)


def get_relevant_context(vault_content, user_input, model_name, embeddings, top_k=3):
    if embeddings.nelement() == 0:
        return []

    input_embedding = ollama.embeddings(model=model_name, prompt=user_input)["embedding"]
    input_embedding_tensor = torch.tensor(input_embedding).unsqueeze(0)

    cos_scores = torch.cosine_similarity(input_embedding_tensor, embeddings)
    top_k = min(top_k, len(cos_scores))
    top_indices = torch.topk(cos_scores, k=top_k)[1].tolist()

    relevant_context = [vault_content[idx].strip() for idx in top_indices]

    return relevant_context


def gpt_chat(user_input, relevant_context, model_name):
    context_str = "\n".join(relevant_context)
    messages = [
        {"role": "system", "content": f"You are a helpful assistant in an online shop. Try to help the user find the best product, using this context:\n{context_str}"},
        {"role": "user", "content": user_input}
    ]
    response = client.chat.completions.create(
        model = model_name,
        messages = messages,
        stream = False,
    )
    return response


app = Flask(__name__)

context_model = "mxbai-embed-large"
main_model = "gpt-3.5-turbo"
file_name = "products.csv"
file_content = open_file(file_name)
file_embeddings = generate_embeddings(file_content, context_model)


@app.route('/query', methods=['POST'])
def query():
    data = request.json
    user_input = data.get('query')

    if not user_input:
        return jsonify({"error": "No query provided"}), 400

    print(f"User input {user_input}")
    relevant_context = get_relevant_context(file_content, user_input, context_model, file_embeddings)
    print(f"Relevant content {relevant_context}")
    response = gpt_chat(user_input, relevant_context, main_model)
    print(f"Response {response}")
    return jsonify({"response": response})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
