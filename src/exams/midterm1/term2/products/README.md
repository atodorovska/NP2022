<p>Да се дефинира класа <code>ShoppingCart</code>&nbsp;за репрезентација&nbsp;на една потрошувачка кошничка во која може да се наоѓаат ставки од 2 типа (ставка која содржи продукт кој се купува во целост, или ставка која содржи продукт кој се купува на грам).</p>
<p><span style="font-size: 0.9375rem;">За класата </span><code>ShoppingCart</code><span style="font-size: 0.9375rem;">&nbsp;да се имплементираат следните методи:</span><br></p>
<ul>
    <li>конструктор</li>
    <li><code>void addItem(String itemData)</code> - метод за додавање на ставка во кошничката. Податоците за ставката се дадени во текстуална форма и може да бидат во следните два формати согласно типот на ставката:<ul>
            <li><code>WS;productID;productName;productPrice;quantity</code>&nbsp;(<code>quantity</code>&nbsp;е цел број,&nbsp;<code>productPrice</code>&nbsp;се однесува на цена на 1 продукт)</li>
            <li><code>PS;productID;productName;productPrice;quantity</code> (<code></code><code>quantity</code>&nbsp;е децимален број - во грамови,&nbsp;<code>productPrice</code>&nbsp;се однесува на цена на 1 кг продукт<code></code>)<ul>
                    <li>Со помош на исклучок од тип <code>InvalidOperationException</code>&nbsp;да се спречи додавање на ставка со quantity 0.</li>
                </ul>
            </li>
        </ul>
    </li>
    <li><code>void printShoppingCart(OutputStream os)</code> - метод за печатење на кошничката на излезен поток. Потребно е да се испечатат сите ставки од кошничката подредени според вкупната цена во опаѓачки редослед. Вкупната цена е производ на цената на продуктот кој е во ставката и квантитетот кој е купен по таа цена.</li>
    <li><code>void <span id="docs-internal-guid-89bc6a71-7fff-9058-6657-07b328b2eaee">blackFridayOffer</span>(List&lt;Integer&gt; discountItems, OutputStream os)</code> - метод којшто ќе ја намали цената за 10% на сите продукти чиј што <code>productID&nbsp;</code>се наоѓа во листата <code>discountItems</code>. Потоа, треба да се испечати извештај за вкупната заштеда на секоја ставка каде има продукт на попуст (види тест пример). Да се фрли исклучок од тип <code>InvalidOperationException</code><span style="font-size: 0.9375rem;"> доколку листата&nbsp; со продукти на попуст е празна.</span></li>
</ul>
<p><br></p>
<p><strong><em>Напомена:&nbsp;</em></strong><em><strong>Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети. Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети со најмногу 80% од поените.</strong></em><br></p>