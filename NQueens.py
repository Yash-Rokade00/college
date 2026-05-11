N = 4

def printSolution(board):
    for i in range(N):
        for j in range(N):
            print(board[i][j], end=' ')
        print()
    print()   # extra line for clarity


def isSafe(board, row, col):

    # Check left side of current row
    for i in range(col):
        if board[row][i] == 1:
            return False

    # Check upper-left diagonal
    for i, j in zip(range(row, -1, -1),
                    range(col, -1, -1)):
        if board[i][j] == 1:
            return False

    # Check lower-left diagonal
    for i, j in zip(range(row, N, 1),
                    range(col, -1, -1)):
        if board[i][j] == 1:
            return False

    return True


def solveNQUtil(board, col):

    # Base Case
    if col >= N:
        printSolution(board)
        return True

    res = False

    # Try placing queen in all rows
    for i in range(N):

        if isSafe(board, i, col):

            # Place queen
            board[i][col] = 1

            # Recur for next column
            res = solveNQUtil(board, col + 1) or res

            # Backtrack
            board[i][col] = 0

    return res


def solveNQ():

    board = [[0] * N for _ in range(N)]

    if not solveNQUtil(board, 0):
        print("Solution does not exist")


solveNQ()