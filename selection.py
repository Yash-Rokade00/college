# Selection Sort Program

def selection_sort(arr):
    n = len(arr)

    # Traverse through all array elements
    for i in range(n):

        # Assume current index has minimum value
        min_index = i

        # Find the minimum element in remaining array
        for j in range(i + 1, n):
            if arr[j] < arr[min_index]:
                min_index = j

        # Swap the found minimum element with first element
        arr[i], arr[min_index] = arr[min_index], arr[i]

    return arr


# Example
numbers = [64, 25, 12, 22, 11]

print("Original Array:", numbers)

selection_sort(numbers)

print("Sorted Array:", numbers)