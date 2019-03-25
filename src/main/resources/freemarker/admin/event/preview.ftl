<#include '../template.ftl'>
<@html>
    <section>
        <h1>${event.name}</h1>
    </section>
    <section>
        <form action="${save}" method="post">
            <label class="warning">Pārbaudi rezultātus un saglabā!</label>
            <input type="submit" value="Saglabāt" style="float: right">
            <input type="hidden" name="file" value="${file}">
        </form>
    </section>
    <#list classes as class>
        <section class="preview">
            <h2>${class.name}</h2>
            <table>
                <tbody>
                    <tr>
                        <th>&nbsp;</th>
                        <th class="name">Vārds, uzvārds</th>
                        <th class="left">Klubs</th>
                        <#if class.courses??><#list class.courses as course><th class="time">${course}</th></#list></#if>
                        <th class="time">Laiks</th>
                        <th class="points">Punkti</th>
                    </tr>
                    <#list class.runners as runner>
                        <tr>
                            <td class="position"><#if runner.overall.position gt 0>${runner.overall.position}<#else>&nbsp;</#if></td>
                            <td class="name">${runner.name}</td>
                            <td class="left">${runner.club}</td>
                            <#if class.courses??>
                                <#list runner.results as result>
                                    <#if result.time gt 0>
                                        <td class="time"><@time seconds=result.time /></td>
                                    <#else>
                                        <td class="status">${result.status}</td>
                                    </#if>
                                </#list>
                            </#if>
                            <#if runner.overall.time gt 0>
                                <td class="time"><@time seconds=runner.overall.time /></td>
                            <#else>
                                <td class="status">${runner.overall.status}</td>
                            </#if>
                            <td class="points"><#if runner.points gt 0>${runner.points}<#else>&nbsp;</#if></td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </section>
    </#list>
</@>
