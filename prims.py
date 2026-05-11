# Prim's Algorithm in Python

INF = 9999999

# Number of vertices
V = 5

# Graph represented using adjacency matrix
graph = [
    [0, 2, 0, 6, 0],
    [2, 0, 3, 8, 5],
    [0, 3, 0, 0, 7],
    [6, 8, 0, 0, 9],
    [0, 5, 7, 9, 0]
]

# Track selected vertices
selected = [False] * V

# Start from vertex 0
selected[0] = True

print("Edge : Weight")

# Number of edges in MST
edge_count = 0

while edge_count < V - 1:

    minimum = INF
    x = 0
    y = 0

    for i in range(V):
        if selected[i]:

            for j in range(V):

                # Select minimum edge
                if (not selected[j]) and graph[i][j]:

                    if minimum > graph[i][j]:
                        minimum = graph[i][j]
                        x = i
                        y = j

    print(x, "-", y, ":", graph[x][y])

    selected[y] = True
    edge_count += 1