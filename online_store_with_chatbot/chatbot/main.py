import torch
import ollama
from flask import Flask, request, jsonify


def open_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as infile:
        return infile.readlines()


def generate_embeddings(texts, model_name):
    embeddings = []
    for text in texts:
        response = ollama.embeddings(model=model_name, prompt=text)
        embeddings.append(response["embedding"])
    return torch.tensor(embeddings)


def get_relevant_context(user_input, vault_file, model_name, top_k=3):
    vault_content = open_file(vault_file)

    vault_embeddings = generate_embeddings(vault_content, model_name)

    if vault_embeddings.nelement() == 0:
        return []

    input_embedding = ollama.embeddings(model=model_name, prompt=user_input)["embedding"]
    input_embedding_tensor = torch.tensor(input_embedding).unsqueeze(0)

    cos_scores = torch.cosine_similarity(input_embedding_tensor, vault_embeddings)
    top_k = min(top_k, len(cos_scores))
    top_indices = torch.topk(cos_scores, k=top_k)[1].tolist()

    relevant_context = [vault_content[idx].strip() for idx in top_indices]

    return relevant_context


def ollama_chat(user_input, relevant_context, ollama_model):
    context_str = "\n".join(relevant_context)
    messages = [
        {"role": "system", "content": f"You are a helpful assistant in an online shop. Try to help the user find the best product. Relevant Context:\n{context_str}"},
        {"role": "user", "content": user_input}
    ]

    response = ollama.chat(model=ollama_model, messages=messages)
    return response['message']['content']


app = Flask(__name__)


@app.route('/query', methods=['POST'])
def query():
    data = request.json
    user_input = data.get('query')
    model_name = "llama3.1"
    # todo product file
    vault_file = "products.txt"

    if not user_input:
        return jsonify({"error": "No query provided"}), 400

    relevant_context = get_relevant_context(user_input, vault_file, model_name)
    response = ollama_chat(user_input, relevant_context, model_name)
    return jsonify({"response": response})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
