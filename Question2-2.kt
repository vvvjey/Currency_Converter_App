fun findMissingNumber(nums: IntArray): Int {
    val n = nums.size
    val total = (n + 1) * (n + 2) / 2

    val sum = nums.sum()

    return total - sum
}

fun main() {
    val nums = intArrayOf(3, 7, 1, 2, 6, 4)
    val missingNumber = findMissingNumber(nums)
    println("The missing number is: $missingNumber")
}
