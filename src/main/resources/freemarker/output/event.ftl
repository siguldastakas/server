<#include 'template.ftl'>
<@html>
    <section>
        <h3>${prefix}</h3>
        <h1>${name}</h1>
        <#list classes as class>
            <section class="results">
                <h2>${class.name}</h2>
                <table>
                    <tbody>
                    <tr>
                        <th>&nbsp;</th>
                        <th class="name">Vārds, uzvārds</th>
                        <th class="club">Klubs</th>
                        <#if class.courses??><#list class.courses as course><th colspan="3">${course}</th></#list></#if>
                        <th colspan="2">Laiks</th>
                        <th class="points">Punkti</th>
                    </tr>
                    <#list class.runners as runner>
                        <tr>
                            <td class="position"><#if runner.overall.position gt 0>${runner.overall.position}<#else>&nbsp;</#if></td>
                            <td class="name">${runner.name}</td>
                            <td class="club">${runner.club}</td>
                            <#if class.courses??>
                                <#list runner.results as result>
                                    <#if result.time gt 0>
                                        <td class="time details"><@time seconds=result.time /></td>
                                        <td class="time-behind details small">+<@time seconds=result.timeBehind /></td>
                                        <td class="position-details details small">(${result.position})</td>
                                    <#else>
                                        <td class="status" colspan="3">${result.status}</td>
                                    </#if>
                                </#list>
                            </#if>
                            <#if runner.overall.time gt 0>
                                <td class="time"><@time seconds=runner.overall.time /></td>
                                <td class="time-behind small">+<@time seconds=runner.overall.timeBehind /></td>
                            <#else>
                                <td class="status" colspan="2">${runner.overall.status}</td>
                            </#if>
                            <td class="points"><#if runner.points gt 0>${runner.points}<#else>&nbsp;</#if></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </section>
        </#list>
    </section>
</@>