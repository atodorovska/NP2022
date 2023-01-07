<p>Да се дефинира генеричка класа за правило (<code>Rule</code>) во која ќе се чуваат имплементации на интерфејсите <code>Predicate</code> и <code>Function</code>. Генеричката класа треба да има два генерички параметри - еден за влезниот тип (типот на објектите кои се спроведуваат низ правилото) и еден за излезниот тип (типот на објектите кои се резултат од правилото). </p>
<p>Во класата <code>Rule</code> да се дефинира метод <code>apply</code> кој прима еден аргумент <code>input</code> од влезниот тип, а враќа објект од генеричката класата <code>Optional</code> со генерички параметар ист како излезниот тип на класата <code>Rule</code>. Методот apply треба да врати <code>Optional</code> објект пополнет со резултатот добиен од <code>Function</code> имплементацијата применет на аргументот <code>input</code> само доколку е исполнет предикатот од правилото <code>Rule</code>. Доколку предикатот не е исполнет, методот <code>apply</code> враќа празен <code>Optional</code>.</p>
<p>Дополнително, да се дефинира класа <code>RuleProcessor</code> со еден генерички статички метод <code>process</code> кој ќе прими два аргументи:</p>
<ul>
    <li>Листа од влезни податоци (објекти од влезниот тип)</li>
    <li>Листа од правила (објекти од класа <code>Rule</code>)</li>
</ul>
<p>Методот потребно е врз секој елемент од листата на влезни податоци да го примени секое правило од листата на правила и на екран да го испечати резултатот од примената на правилото (доколку постои), а во спротивно да испечати порака <code>Condition not met</code>.</p>
<p>Во главната класа на местата означени со <code>TODO</code> да се дефинираат потребните објекти од класата <code>Rule</code>. Да се користат ламбда изрази за дефинирање на објекти од тип&nbsp;<code>Predicate</code>&nbsp;и&nbsp;<code>Function.</code></p>
<p><br></p>
<p><strong><em>Напомена:&nbsp;</em></strong><em><strong>Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети. Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети со најмногу 80% од поените.</strong></em><br></p>