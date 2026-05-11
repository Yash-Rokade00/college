# Kruskal's Algorithm in Python

# List of edges in the format:
# (weight, source, destination)

edges = [
    (2, 0, 1),
    (3, 1, 2),
    (6, 0, 3),
    (8, 1, 3)
]

# Sort edges based on weight
edges.sort()

# Parent array for Union-Find
parent = [0, 1, 2, 3]

# Find function
def find(vertex):

    # If vertex is its own parent
    if parent[vertex] == vertex:
        return vertex

    # Recursively find parent
    return find(parent[vertex])


# Union function
def union(u, v):

    parent_u = find(u)
    parent_v = find(v)

    # Connect two sets
    parent[parent_v] = parent_u


print("Edge : Weight")

# Process all edges
for edge in edges:

    weight, u, v = edge

    # Check if cycle is formed
    if find(u) != find(v):

        print(u, "-", v, ":", weight)

        # Join sets
        union(u, v)