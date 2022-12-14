<p>Да се дефинира класа <code>Quiz</code> за репрезентација на еден квиз во кој може да се наоѓаат повеќе прашања од 2 типа (<code>true/false</code> прашање или прашање со избор на еден точен одговор од 5 понудени одговори <code>A/B/C/D/E</code>). За класата <code>Quiz</code> да се имплементираат следните методи:</p>
<ul>
    <li>конструктор</li>
    <li><code>void addQuestion(String questionData)</code> - метод за додавање на прашање во квизот. Податоците за прашањето се дадени во текстуална форма и може да бидат во следните два формати согласно типот на прашањето:<ul>
            <li><code>TF;text;points;answer</code> (<code>answer</code> може да биде <code>true</code> или <code>false</code>)</li>
            <li><code>MC;text;points;answer</code> (<code>answer</code> е каракатер кој може да ја има вредноста <code>A/B/C/D/E</code>)<ul>
                    <li>Со помош на исклучок од тип <code>InvalidOperationException</code> да се спречи додавање на прашање со повеќе понудени одговори во кое како точен одговор се наоѓа некоја друга опција освен опциите <code>A/B/C/D/E</code>. </li>
                </ul>
            </li>
        </ul>
    </li>
    <li><code>void printQuiz(OutputStream os)</code> - метод за печатење на квизот на излезен поток. Потребно е да се испечатат сите прашања од квизот подредени според бројот на поени на прашањата во опаѓачки редослед.</li>
    <li><code>void answerQuiz (List&lt;String&gt; answers, OutputStream os)</code> - метод којшто ќе ги одговори сите прашања од квизот (во редоследот во којшто се внесени) со помош на одговорите од листата answers. Методот треба да испечати извештај по колку поени се освоени од секое прашање и колку вкупно поени се освоени од квизот (види тест пример). Да се фрли исклучок од тип <code>InvalidOperationException</code> доколку бројот на одговорите во `answers е различен од број на прашања во квизот. </li>
    <ul>
        <li>За точен одговор на true/false прашање се добиваат сите предвидени поени, а за неточен одговор се добиваат 0 поени</li>
        <li>За точен одговор на прашање со повеќе понудени одговори се добиваат сите предвидени поени, а за неточен одговор се добиваат негативни поени (20% од предвидените поени).</li>
    </ul>

</ul>
<p><strong><em><br></em></strong></p>
<p><strong><em>Напомена: </em></strong><em><strong>Решенијата кои нема да може да се извршат (не компајлираат) нема да бидат оценети. Дополнително, решенијата кои не се дизајнирани правилно според принципите на ООП ќе се оценети со најмногу 80% од поените.</strong></em></p>