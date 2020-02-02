<#include 'template.ftl'>
<@html>
    <section>
        <h3>${prefix}</h3>
        <h1>Kopvērtējums</h1>
        <#list classes as class>
            <section class="results">
                <h2>${class.name}</h2>
                <table>
                    <tbody>
                    <tr>
                        <th>&nbsp;</th>
                        <th class="name">Vārds, uzvārds</th>
                        <th class="left">Klubs</th>
                        <th class="points">Punkti</th>
                        <#list events as event><th class="points">${event.number}. kārta</th></#list>
                    </tr>
                    <#list class.runners as runner>
                        <tr>
                            <td class="position"><#if runner.points gt 0>${runner.position}<#else>&nbsp;</#if></td>
                            <td class="name">${runner.name}</td>
                            <td class="left">${runner.club}</td>
                            <td class="points"><#if runner.points gt 0>${runner.points}<#else>&nbsp;</#if></td>
                            <#list runner.eventPoints as points>
                                <#if points??>
                                    <td class="points details<#if !points.used> small</#if>">${points.points}</td>
                                <#else>
                                    <td>&nbsp;</td>
                                </#if>
                            </#list>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </section>
        </#list>
    </section>
</@>