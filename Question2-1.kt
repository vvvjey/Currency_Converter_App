// Define class
data class Product(val name: String, val price: Double, val quantity: Int) {
    fun totalValue(): Double {
        return price * quantity
    }
}

// Hàm tính tổng giá trị kho
fun totalInventoryValue(products: List<Product>): Double {
    return products.sumOf { it.totalValue() }
}

// Find the most expensive product
fun mostExpensiveProduct(products: List<Product>): String {
    val mostExpensive = products.maxByOrNull { it.price }
    return mostExpensive?.name ?: "No products available"
}

// Check if product named "Headphones" is exist
fun checkProductInStock(products: List<Product>, productName: String): Boolean {
    return products.any { it.name.equals(productName, ignoreCase = true) && it.quantity > 0 }
}

// Sort by price or quantity
fun sortProducts(products: List<Product>, sortBy: String, ascending: Boolean = true): List<Product> {
    return when(sortBy.lowercase()) {
        "price" -> if (ascending) products.sortedBy { it.price } else products.sortedByDescending { it.price }
        "quantity" -> if (ascending) products.sortedBy { it.quantity } else products.sortedByDescending { it.quantity }
        else -> products
    }
}

fun main() {
    val products = listOf(
        Product("Laptop", 999.99, 5),
        Product("Smartphone", 499.99, 10),
        Product("Tablet", 299.99, 0),
        Product("Smartwatch", 199.99, 3)
    )

    println("Total inventory value: ${totalInventoryValue(products)}")

    println("Most expensive product: ${mostExpensiveProduct(products)}")

    val productName = "Headphones"
    println("Is $productName in stock? ${checkProductInStock(products, productName)}")

    val sortedByPrice = sortProducts(products, "price", ascending = false)
    println("Products sorted by price (descending):")
    sortedByPrice.forEach { println("${it.name}: ${it.price}, Quantity: ${it.quantity}") }

    val sortedByQuantity = sortProducts(products, "quantity", ascending = true)
    println("\nProducts sorted by quantity (ascending):")
    sortedByQuantity.forEach { println("${it.name}: ${it.price}, Quantity: ${it.quantity}") }
}
