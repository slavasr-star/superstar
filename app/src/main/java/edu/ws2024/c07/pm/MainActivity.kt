package edu.ws2024.c07.pm

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import java.util.Locale

class MainActivity : Activity() {

    private val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
    private val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT

    private val green = Color.rgb(139, 204, 55)
    private val yellow = Color.rgb(255, 204, 0)
    private val gray = Color.rgb(120, 120, 120)

    private var selectedCategory = 2
    private var seatNumber = "23"
    private var dinerCount = 1

    private val cart = mutableMapOf<String, Int>()

    data class Food(
        val name: String,
        val image: Int,
        val price: Double,
        val oldPrice: Double,
        val category: Int
    )

    private val foods by lazy {
        listOf(
            Food("Baguette", R.drawable.baguette, 2.00, 3.38, 0),
            Food("Croissant", R.drawable.croissant, 5.38, 10.38, 0),
            Food("La galette", R.drawable.la_galette, 15.38, 20.38, 0),
            Food("Pain brie", R.drawable.pain_brie, 7.80, 12.38, 0),

            Food("French onion soup", R.drawable.french_onion_soup, 20.00, 30.38, 1),
            Food("Bouillabaisse", R.drawable.bouillabaisse, 5.38, 10.38, 1),
            Food("French vegetable beef soup", R.drawable.french_vegetable_beef_soup, 15.38, 20.38, 1),
            Food("Pumpkin soup", R.drawable.pumpkin_soup, 7.80, 12.38, 1),

            Food("Basque chicken stew", R.drawable.basque_chicken_stew, 20.00, 30.38, 2),
            Food("French snail", R.drawable.french_snail, 5.38, 10.38, 2),
            Food("Cassoulet", R.drawable.cassoulet, 15.38, 20.38, 2),
            Food("Frog legs", R.drawable.frog_legs, 7.80, 12.38, 2)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        showWelcome()
    }

    private fun showWelcome() {
        val root = FrameLayout(this)

        val bg = ImageView(this)
        bg.setImageResource(R.drawable.welcome_bg)
        bg.scaleType = ImageView.ScaleType.CENTER_CROP
        root.addView(bg, flp(MATCH, MATCH))

        val dark = View(this)
        dark.setBackgroundColor(Color.argb(80, 0, 0, 0))
        root.addView(dark, flp(MATCH, MATCH))

        val logo = ImageView(this)
        logo.setImageResource(R.drawable.logo)
        logo.scaleType = ImageView.ScaleType.FIT_CENTER
        root.addView(logo, flp(dp(120), dp(120), Gravity.TOP or Gravity.LEFT, 65, 35, 0, 0))

        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.gravity = Gravity.CENTER_HORIZONTAL
        card.setPadding(dp(45), dp(25), dp(45), dp(25))
        card.background = round(Color.argb(150, 0, 0, 0), 8)

        val title = tv("Thank you for\nchoosing to dine here.", 42f, Color.WHITE, Typeface.NORMAL)
        title.gravity = Gravity.CENTER
        card.addView(title, llp(MATCH, WRAP))

        val seatLabel = tv("Seat number", 22f, green, Typeface.NORMAL)
        card.addView(seatLabel, llp(MATCH, WRAP, 0f, 0, 25, 0, 5))

        val seatInput = EditText(this)
        seatInput.hint = "Please enter your seat number"
        seatInput.setTextColor(Color.DKGRAY)
        seatInput.setHintTextColor(Color.GRAY)
        seatInput.textSize = 22f
        seatInput.gravity = Gravity.CENTER
        seatInput.inputType = InputType.TYPE_CLASS_NUMBER
        seatInput.background = round(Color.WHITE, 10)
        card.addView(seatInput, llp(MATCH, dp(70), 0f, 0, 0, 0, 25))

        val dinerLabel = tv("Number of diner(s)", 22f, green, Typeface.NORMAL)
        card.addView(dinerLabel, llp(MATCH, WRAP, 0f, 0, 0, 0, 10))

        val dinerRow = LinearLayout(this)
        dinerRow.orientation = LinearLayout.HORIZONTAL
        dinerRow.gravity = Gravity.CENTER

        for (i in 1..4) {
            val b = smallWhiteButton(i.toString())
            b.setOnClickListener { dinerCount = i }
            dinerRow.addView(b, llp(dp(120), dp(48), 0f, 10, 0, 10, 0))
        }

        card.addView(dinerRow, llp(MATCH, WRAP))

        val confirm = Button(this)
        confirm.text = "Confirm"
        confirm.textSize = 30f
        confirm.setTextColor(Color.rgb(210, 120, 45))
        confirm.background = round(Color.WHITE, 5, Color.GRAY, 1)
        confirm.setOnClickListener {
            seatNumber = seatInput.text.toString().ifBlank { "23" }
            showHome()
        }
        card.addView(confirm, llp(dp(340), dp(80), 0f, 0, 35, 0, 0))

        root.addView(card, flp(dp(760), dp(610), Gravity.CENTER))

        setContentView(root)
    }

    private fun showHome() {
        setContentView(shell(homeContent(), "home"))
    }

    private fun homeContent(): View {
        val root = FrameLayout(this)

        val main = LinearLayout(this)
        main.orientation = LinearLayout.VERTICAL
        root.addView(main, flp(MATCH, MATCH))

        val categoryFoods = foods.filter { it.category == selectedCategory }

        val topImages = LinearLayout(this)
        topImages.orientation = LinearLayout.HORIZONTAL
        topImages.gravity = Gravity.CENTER

        for (food in categoryFoods.take(3)) {
            val img = ImageView(this)
            img.setImageResource(food.image)
            img.scaleType = ImageView.ScaleType.CENTER_CROP
            topImages.addView(img, llp(0, dp(235), 1f, 25, 30, 25, 10))
        }

        main.addView(topImages, llp(MATCH, dp(300)))

        val scroll = HorizontalScrollView(this)
        scroll.isHorizontalScrollBarEnabled = false

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_VERTICAL

        for (food in categoryFoods) {
            row.addView(foodCard(food), llp(dp(185), dp(235), 0f, 35, 15, 35, 0))
        }

        scroll.addView(row)
        main.addView(scroll, llp(MATCH, dp(280)))

        val cartButton = FrameLayout(this)
        cartButton.background = round(Color.WHITE, 100)
        cartButton.elevation = 10f
        cartButton.setOnClickListener { showCart() }

        val cartIcon = tv("🛒", 38f, gray, Typeface.NORMAL)
        cartIcon.gravity = Gravity.CENTER
        cartButton.addView(cartIcon, flp(MATCH, MATCH))

        val count = cart.values.sum()
        if (count > 0) {
            val badge = tv(count.toString(), 13f, Color.WHITE, Typeface.BOLD)
            badge.gravity = Gravity.CENTER
            badge.background = round(Color.RED, 50)
            cartButton.addView(badge, flp(dp(28), dp(28), Gravity.TOP or Gravity.RIGHT, 0, 0, 0, 0))
        }

        root.addView(cartButton, flp(dp(95), dp(95), Gravity.RIGHT or Gravity.CENTER_VERTICAL, 0, 0, 35, 0))

        return root
    }

    private fun showCart() {
        val content = FrameLayout(this)

        val close = tv("×", 42f, Color.LTGRAY, Typeface.NORMAL)
        close.gravity = Gravity.CENTER
        close.setOnClickListener { showHome() }
        content.addView(close, flp(dp(70), dp(70), Gravity.TOP or Gravity.RIGHT, 0, 10, 20, 0))

        val handle = View(this)
        handle.background = round(Color.GRAY, 20)
        content.addView(handle, flp(dp(110), dp(12), Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 18, 0, 0))

        val scroll = ScrollView(this)
        val list = LinearLayout(this)
        list.orientation = LinearLayout.VERTICAL
        list.setPadding(dp(60), dp(35), dp(60), dp(130))

        val items = cartItems()
        if (items.isEmpty()) {
            val empty = tv("Shopping cart is empty", 28f, Color.GRAY, Typeface.NORMAL)
            empty.gravity = Gravity.CENTER
            list.addView(empty, llp(MATCH, dp(220)))
        } else {
            for ((food, qty) in items) {
                list.addView(cartRow(food, qty, true, false), llp(MATCH, dp(120), 0f, 0, 10, 0, 10))
                list.addView(line(), llp(MATCH, dp(1)))
            }
        }

        scroll.addView(list)
        content.addView(scroll, flp(MATCH, MATCH))

        val bottom = LinearLayout(this)
        bottom.orientation = LinearLayout.HORIZONTAL
        bottom.gravity = Gravity.CENTER
        bottom.background = round(Color.WHITE, 18)
        bottom.elevation = 8f

        val total = tv(money(totalPrice()), 28f, Color.DKGRAY, Typeface.NORMAL)
        total.gravity = Gravity.CENTER
        bottom.addView(total, llp(dp(180), MATCH))

        val submit = Button(this)
        submit.text = "Submit"
        submit.textSize = 24f
        submit.setTextColor(Color.WHITE)
        submit.background = round(yellow, 18)
        submit.setOnClickListener { showPayment() }
        bottom.addView(submit, llp(dp(180), MATCH))

        content.addView(bottom, flp(dp(380), dp(80), Gravity.RIGHT or Gravity.BOTTOM, 0, 0, 45, 25))

        setContentView(shell(content, "home"))
    }

    private fun showPayment() {
        val root = FrameLayout(this)
        root.setBackgroundColor(Color.WHITE)

        val title = tv("Payment", 42f, Color.DKGRAY, Typeface.NORMAL)
        title.gravity = Gravity.CENTER
        root.addView(title, flp(MATCH, dp(80), Gravity.TOP))

        val close = tv("×", 45f, Color.LTGRAY, Typeface.NORMAL)
        close.gravity = Gravity.CENTER
        close.setOnClickListener { showCart() }
        root.addView(close, flp(dp(70), dp(70), Gravity.TOP or Gravity.RIGHT, 0, 5, 15, 0))

        val body = LinearLayout(this)
        body.orientation = LinearLayout.HORIZONTAL
        body.setPadding(dp(35), dp(20), dp(35), dp(35))
        root.addView(body, flp(MATCH, MATCH, Gravity.TOP, 0, 85, 0, 0))

        val leftScroll = ScrollView(this)
        val leftList = LinearLayout(this)
        leftList.orientation = LinearLayout.VERTICAL

        val items = cartItems()
        if (items.isEmpty()) {
            leftList.addView(tv("No food selected", 26f, Color.GRAY, Typeface.NORMAL), llp(MATCH, dp(100)))
        } else {
            for ((food, qty) in items) {
                leftList.addView(cartRow(food, qty, false, false), llp(MATCH, dp(130), 0f, 0, 5, 0, 5))
                leftList.addView(line(), llp(MATCH, dp(1)))
            }
        }

        leftScroll.addView(leftList)
        body.addView(leftScroll, llp(0, MATCH, 1f, 0, 0, 30, 0))

        val panel = LinearLayout(this)
        panel.orientation = LinearLayout.VERTICAL
        panel.setPadding(dp(28), dp(28), dp(28), dp(20))
        panel.background = round(Color.rgb(235, 235, 235), 16, yellow, 1)

        val seatRow = LinearLayout(this)
        seatRow.orientation = LinearLayout.HORIZONTAL
        seatRow.gravity = Gravity.CENTER_VERTICAL

        seatRow.addView(tv("Seat number:", 28f, Color.DKGRAY, Typeface.NORMAL), llp(0, WRAP, 1f))

        val seatInput = EditText(this)
        seatInput.setText(seatNumber)
        seatInput.textSize = 40f
        seatInput.gravity = Gravity.CENTER
        seatInput.inputType = InputType.TYPE_CLASS_NUMBER
        seatInput.background = round(Color.WHITE, 15)
        seatRow.addView(seatInput, llp(dp(190), dp(75)))

        panel.addView(seatRow, llp(MATCH, dp(95)))

        val payRow = LinearLayout(this)
        payRow.orientation = LinearLayout.HORIZONTAL
        payRow.gravity = Gravity.CENTER_VERTICAL

        payRow.addView(tv("Payment required:", 28f, Color.DKGRAY, Typeface.NORMAL), llp(0, WRAP, 1f))
        payRow.addView(tv(money(totalPrice()), 40f, Color.DKGRAY, Typeface.NORMAL), llp(WRAP, WRAP))

        panel.addView(payRow, llp(MATCH, dp(95)))

        val methodRow = LinearLayout(this)
        methodRow.orientation = LinearLayout.HORIZONTAL
        methodRow.gravity = Gravity.CENTER_VERTICAL

        methodRow.addView(tv("Payment method:", 27f, Color.DKGRAY, Typeface.NORMAL), llp(0, WRAP, 1f))

        val group = RadioGroup(this)
        group.orientation = RadioGroup.HORIZONTAL

        val paypal = RadioButton(this)
        paypal.text = "PayPal"
        paypal.textSize = 20f
        paypal.isChecked = true

        val visa = RadioButton(this)
        visa.text = "Visa card"
        visa.textSize = 20f

        group.addView(paypal)
        group.addView(visa)

        methodRow.addView(group, llp(WRAP, WRAP))
        panel.addView(methodRow, llp(MATCH, dp(90)))

        val checkout = Button(this)
        checkout.text = "Check out"
        checkout.textSize = 28f
        checkout.setTextColor(Color.WHITE)
        checkout.background = round(green, 15)
        checkout.setOnClickListener {
            seatNumber = seatInput.text.toString().ifBlank { "23" }
            showOrdered()
        }

        panel.addView(checkout, llp(dp(360), dp(80), 0f, 100, 10, 100, 10))

        val hint = tv("Please confirm the seat number before your payment.", 18f, Color.DKGRAY, Typeface.NORMAL)
        hint.gravity = Gravity.CENTER
        panel.addView(hint, llp(MATCH, WRAP, 0f, 0, 10, 0, 0))

        body.addView(panel, llp(dp(590), dp(440), 0f, 0, 0, 0, 0))

        setContentView(root)
    }

    private fun showOrdered() {
        val content = FrameLayout(this)

        val title = tv("Ordered", 42f, yellow, Typeface.NORMAL)
        title.gravity = Gravity.CENTER
        content.addView(title, flp(MATCH, dp(85), Gravity.TOP))

        val scroll = ScrollView(this)
        val list = LinearLayout(this)
        list.orientation = LinearLayout.VERTICAL
        list.setPadding(dp(25), dp(20), dp(25), dp(20))

        val items = cartItems()
        if (items.isEmpty()) {
            val empty = tv("No ordered food", 28f, Color.GRAY, Typeface.NORMAL)
            empty.gravity = Gravity.CENTER
            list.addView(empty, llp(MATCH, dp(220)))
        } else {
            for ((food, qty) in items) {
                list.addView(cartRow(food, qty, false, true), llp(MATCH, dp(135), 0f, 0, 5, 0, 5))
                list.addView(line(), llp(MATCH, dp(1)))
            }
        }

        scroll.addView(list)
        content.addView(scroll, flp(MATCH, MATCH, Gravity.TOP, 0, 90, 0, 0))

        setContentView(shell(content, "ordered"))
    }

    private fun shell(content: View, selectedBottom: String): View {
        val root = FrameLayout(this)

        val main = LinearLayout(this)
        main.orientation = LinearLayout.HORIZONTAL
        root.addView(main, flp(MATCH, MATCH, Gravity.TOP, 0, 0, 0, 92))

        val side = LinearLayout(this)
        side.orientation = LinearLayout.VERTICAL
        side.gravity = Gravity.CENTER_HORIZONTAL
        side.setBackgroundColor(Color.WHITE)
        side.elevation = 8f

        side.addView(sideItem("Main dishes", "🍳", selectedCategory == 2) {
            selectedCategory = 2
            showHome()
        }, llp(MATCH, dp(180), 0f, 0, 35, 0, 10))

        side.addView(sideItem("Pastries", "🥖", selectedCategory == 0) {
            selectedCategory = 0
            showHome()
        }, llp(MATCH, dp(180), 0f, 0, 10, 0, 10))

        side.addView(sideItem("Soups", "🍲", selectedCategory == 1) {
            selectedCategory = 1
            showHome()
        }, llp(MATCH, dp(180), 0f, 0, 10, 0, 10))

        main.addView(side, llp(dp(125), MATCH))
        main.addView(content, llp(0, MATCH, 1f))

        val bottom = LinearLayout(this)
        bottom.orientation = LinearLayout.HORIZONTAL
        bottom.gravity = Gravity.CENTER
        bottom.setBackgroundColor(Color.WHITE)
        bottom.elevation = 10f

        bottom.addView(bottomItem("☰", "Ordered", selectedBottom == "ordered") {
            showOrdered()
        }, llp(0, MATCH, 1f))

        bottom.addView(bottomItem("⌂", "Home", selectedBottom == "home") {
            showHome()
        }, llp(0, MATCH, 1f))

        root.addView(bottom, flp(MATCH, dp(92), Gravity.BOTTOM))

        return root
    }

    private fun sideItem(label: String, icon: String, selected: Boolean, click: () -> Unit): View {
        val box = LinearLayout(this)
        box.orientation = LinearLayout.VERTICAL
        box.gravity = Gravity.CENTER
        box.setOnClickListener { click() }

        val i = tv(icon, 34f, Color.DKGRAY, Typeface.NORMAL)
        i.gravity = Gravity.CENTER
        box.addView(i, llp(MATCH, WRAP))

        if (selected) {
            val t = tv(label, 20f, gray, Typeface.BOLD)
            t.gravity = Gravity.CENTER
            box.addView(t, llp(MATCH, WRAP))
        }

        return box
    }

    private fun bottomItem(icon: String, label: String, selected: Boolean, click: () -> Unit): View {
        val box = LinearLayout(this)
        box.orientation = LinearLayout.VERTICAL
        box.gravity = Gravity.CENTER
        box.setOnClickListener { click() }

        val i = tv(icon, 38f, gray, Typeface.NORMAL)
        i.gravity = Gravity.CENTER
        box.addView(i, llp(MATCH, WRAP))

        if (selected) {
            val t = tv(label, 22f, gray, Typeface.BOLD)
            t.gravity = Gravity.CENTER
            box.addView(t, llp(MATCH, WRAP))
        }

        return box
    }

    private fun foodCard(food: Food): View {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        card.background = round(Color.WHITE, 10, yellow, 1)
        card.elevation = 5f
        card.setPadding(dp(8), dp(8), dp(8), dp(8))

        val img = ImageView(this)
        img.setImageResource(food.image)
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        card.addView(img, llp(MATCH, dp(92)))

        val name = tv(food.name, 18f, Color.DKGRAY, Typeface.NORMAL)
        name.maxLines = 2
        card.addView(name, llp(MATCH, WRAP, 0f, 0, 7, 0, 0))

        val desc = tv("Delicious, tasty", 13f, gray, Typeface.NORMAL)
        card.addView(desc, llp(MATCH, WRAP))

        val bottom = LinearLayout(this)
        bottom.orientation = LinearLayout.HORIZONTAL
        bottom.gravity = Gravity.CENTER_VERTICAL

        val price = priceRow(food)
        bottom.addView(price, llp(0, WRAP, 1f))

        val q = qty(food)

        if (q == 0) {
            val plus = miniButton("+")
            plus.setOnClickListener {
                addFood(food)
                showHome()
            }
            bottom.addView(plus, llp(dp(28), dp(28)))
        } else {
            bottom.addView(qtyControl(food), llp(dp(80), dp(28)))
        }

        card.addView(bottom, llp(MATCH, WRAP, 0f, 0, 6, 0, 0))

        return card
    }

    private fun cartRow(food: Food, qty: Int, editable: Boolean, successful: Boolean): View {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_VERTICAL
        row.setPadding(dp(5), dp(5), dp(5), dp(5))

        val img = ImageView(this)
        img.setImageResource(food.image)
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        row.addView(img, llp(dp(160), dp(95), 0f, 0, 0, 18, 0))

        val info = LinearLayout(this)
        info.orientation = LinearLayout.VERTICAL

        val name = tv(food.name, 26f, Color.DKGRAY, Typeface.NORMAL)
        name.maxLines = 2
        info.addView(name, llp(MATCH, WRAP))

        info.addView(tv("Delicious, tasty", 18f, gray, Typeface.NORMAL), llp(MATCH, WRAP, 0f, 0, 5, 0, 10))
        info.addView(priceRow(food), llp(MATCH, WRAP))

        row.addView(info, llp(0, WRAP, 1f))

        if (successful) {
            val ok = tv("Successful", 24f, Color.DKGRAY, Typeface.NORMAL)
            ok.gravity = Gravity.CENTER
            row.addView(ok, llp(dp(210), WRAP))
        } else if (editable) {
            row.addView(qtyControl(food), llp(dp(110), dp(32), 0f, 15, 0, 10, 0))
        } else {
            val badge = tv(qty.toString(), 13f, Color.WHITE, Typeface.BOLD)
            badge.gravity = Gravity.CENTER
            badge.background = round(green, 4)
            row.addView(badge, llp(dp(26), dp(26), 0f, 15, 0, 15, 0))
        }

        return row
    }

    private fun qtyControl(food: Food): View {
        val box = LinearLayout(this)
        box.orientation = LinearLayout.HORIZONTAL
        box.gravity = Gravity.CENTER

        val minus = miniButton("−")
        minus.setOnClickListener {
            changeQty(food, -1)
            showHome()
        }

        val number = tv(qty(food).toString(), 13f, Color.WHITE, Typeface.BOLD)
        number.gravity = Gravity.CENTER
        number.background = round(green, 4)

        val plus = miniButton("+")
        plus.setOnClickListener {
            changeQty(food, 1)
            showHome()
        }

        box.addView(minus, llp(dp(26), MATCH))
        box.addView(number, llp(dp(28), MATCH))
        box.addView(plus, llp(dp(26), MATCH))

        return box
    }

    private fun priceRow(food: Food): View {
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER_VERTICAL

        val price = tv(money(food.price), 15f, Color.DKGRAY, Typeface.NORMAL)
        row.addView(price, llp(WRAP, WRAP, 0f, 0, 0, 5, 0))

        val old = tv(money(food.oldPrice), 12f, Color.GRAY, Typeface.NORMAL)
        old.paintFlags = old.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        row.addView(old, llp(WRAP, WRAP))

        return row
    }

    private fun miniButton(text: String): TextView {
        val b = tv(text, 16f, Color.DKGRAY, Typeface.BOLD)
        b.gravity = Gravity.CENTER
        b.background = round(Color.WHITE, 3, Color.LTGRAY, 1)
        return b
    }

    private fun smallWhiteButton(text: String): TextView {
        val b = tv(text, 22f, Color.BLACK, Typeface.NORMAL)
        b.gravity = Gravity.CENTER
        b.background = round(Color.WHITE, 18)
        return b
    }

    private fun tv(text: String, size: Float, color: Int, style: Int): TextView {
        val v = TextView(this)
        v.text = text
        v.textSize = size
        v.setTextColor(color)
        v.typeface = Typeface.create(Typeface.SANS_SERIF, style)
        return v
    }

    private fun line(): View {
        val v = View(this)
        v.setBackgroundColor(Color.LTGRAY)
        return v
    }

    private fun qty(food: Food): Int {
        return cart[food.name] ?: 0
    }

    private fun addFood(food: Food) {
        cart[food.name] = qty(food) + 1
    }

    private fun changeQty(food: Food, delta: Int) {
        val newQty = qty(food) + delta
        if (newQty <= 0) cart.remove(food.name)
        else cart[food.name] = newQty
    }

    private fun cartItems(): List<Pair<Food, Int>> {
        return foods.filter { cart.containsKey(it.name) }.map { it to (cart[it.name] ?: 0) }
    }

    private fun totalPrice(): Double {
        var sum = 0.0
        for ((food, qty) in cartItems()) {
            sum += food.price * qty
        }
        return sum
    }

    private fun money(value: Double): String {
        return "€" + String.format(Locale.US, "%.2f", value)
    }

    private fun round(color: Int, radius: Int, strokeColor: Int? = null, stroke: Int = 0): GradientDrawable {
        val d = GradientDrawable()
        d.setColor(color)
        d.cornerRadius = dp(radius).toFloat()
        if (strokeColor != null) {
            d.setStroke(dp(stroke), strokeColor)
        }
        return d
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    private fun llp(
        w: Int,
        h: Int,
        weight: Float = 0f,
        l: Int = 0,
        t: Int = 0,
        r: Int = 0,
        b: Int = 0
    ): LinearLayout.LayoutParams {
        val p = LinearLayout.LayoutParams(w, h, weight)
        p.setMargins(dp(l), dp(t), dp(r), dp(b))
        return p
    }

    private fun flp(
        w: Int,
        h: Int,
        gravity: Int = Gravity.NO_GRAVITY,
        l: Int = 0,
        t: Int = 0,
        r: Int = 0,
        b: Int = 0
    ): FrameLayout.LayoutParams {
        val p = FrameLayout.LayoutParams(w, h)
        p.gravity = gravity
        p.setMargins(dp(l), dp(t), dp(r), dp(b))
        return p
    }
}