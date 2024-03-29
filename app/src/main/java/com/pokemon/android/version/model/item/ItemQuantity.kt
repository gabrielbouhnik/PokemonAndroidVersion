package com.pokemon.android.version.model.item

class ItemQuantity(var itemId: Int, var quantity: Int) {
    companion object {
        fun createItemQuantityFromHashMap(items: HashMap<Int, Int>): ArrayList<ItemQuantity> {
            val res: ArrayList<ItemQuantity> = arrayListOf()
            for ((key, value) in items) {
                res.add(ItemQuantity(key, value))
            }
            res.sortBy { it.itemId }
            return res
        }
    }
}