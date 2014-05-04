<% binding['values'].each { %>\
insert into emp (no_emp, nm_emp) values ('<%= it[0] %>', '<%= it[1] %>')
/
<% } %>\
commit
/
