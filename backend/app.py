from flask import Flask, jsonify, request

app = Flask(__name__)

# In-memory database for simplicity
posts = [
    {
        "id": 1,
        "title": "Welcome to Your New Blog!",
        "content": "This is your first post. You can edit or delete it."
    }
]
next_id = 2

# Get all posts
@app.route('/api/posts', methods=['GET'])
def get_posts():
    return jsonify(posts)

# Create a new post
@app.route('/api/posts', methods=['POST'])
def create_post():
    global next_id
    data = request.get_json()
    if not data or not 'title' in data or not 'content' in data:
        return jsonify({'error': 'Missing title or content'}), 400

    new_post = {
        'id': next_id,
        'title': data['title'],
        'content': data['content']
    }
    posts.append(new_post)
    next_id += 1
    return jsonify(new_post), 201

# Get a single post
@app.route('/api/posts/<int:post_id>', methods=['GET'])
def get_post(post_id):
    post = next((post for post in posts if post['id'] == post_id), None)
    if post:
        return jsonify(post)
    return jsonify({'error': 'Post not found'}), 404

# Update a post
@app.route('/api/posts/<int:post_id>', methods=['PUT'])
def update_post(post_id):
    post = next((post for post in posts if post['id'] == post_id), None)
    if not post:
        return jsonify({'error': 'Post not found'}), 404

    data = request.get_json()
    if not data:
        return jsonify({'error': 'Bad request'}), 400

    post['title'] = data.get('title', post['title'])
    post['content'] = data.get('content', post['content'])

    return jsonify(post)

# Delete a post
@app.route('/api/posts/<int:post_id>', methods=['DELETE'])
def delete_post(post_id):
    global posts
    post = next((post for post in posts if post['id'] == post_id), None)
    if not post:
        return jsonify({'error': 'Post not found'}), 404

    posts = [p for p in posts if p['id'] != post_id]
    return jsonify({'message': 'Post deleted successfully'}), 200

# Home route for basic check
@app.route('/')
def home():
    return "Backend server is running."

if __name__ == '__main__':
    # Running on 0.0.0.0 makes it accessible from the local network
    app.run(host='0.0.0.0', port=5000, debug=True)