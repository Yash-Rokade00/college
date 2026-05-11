adj_matrix = [
    [0,1,1,1,0,0],
    [1,0,1,0,1,0],
    [1,1,0,1,1,1],
    [1,0,1,0,0,1],
    [0,1,1,0,0,1],
    [0,0,1,1,1,0]
]

nodes = ['a','b','c','d','e','f']
n = len(nodes)

print("Adjacency Matrix Loaded Successfully")

# Calculate Degree of Each Node
degree = [0]*n

for i in range(n):
    degree[i] = sum(adj_matrix[i])

print("Degree of Nodes:")
for i in range(n):
    print(nodes[i], "=", degree[i])

# Sort Nodes by Degree (Descending Order)
sorted_nodes = nodes.copy()
sorted_degree = degree.copy()

for i in range(n):
    max_idx = i
    for j in range(i+1, n):
        if sorted_degree[j] > sorted_degree[max_idx]:
            max_idx = j

    # Swap degree
    sorted_degree[i], sorted_degree[max_idx] = sorted_degree[max_idx], sorted_degree[i]

    # Swap nodes
    sorted_nodes[i], sorted_nodes[max_idx] = sorted_nodes[max_idx], sorted_nodes[i]

print("Nodes sorted by degree:")
print(sorted_nodes)

# Graph Coloring using Greedy Algorithm
colors = {}
available_colors = ["Red","Blue","Green","Yellow","Orange"]

for node in sorted_nodes:
    index = nodes.index(node)

    # Find colors used by adjacent nodes
    used_colors = set()
    for j in range(n):
        if adj_matrix[index][j] == 1:
            neighbor = nodes[j]
            if neighbor in colors:
                used_colors.add(colors[neighbor])

    # Assign first available color
    for color in available_colors:
        if color not in used_colors:
            colors[node] = color
            break

print("Graph Coloring Solution:")
for node in nodes:
    print("Node", node, "=", colors[node])