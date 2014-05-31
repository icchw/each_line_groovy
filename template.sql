<% 
  binding['values'].each {
    if (it.size < 4 || it[3] != '›') {
      return
    }
%>\
insert into emp (no_emp, nm_emp) values ('<%= it[1] %>', '<%= it[2] %>')
/
<% } %>\
commit
/
