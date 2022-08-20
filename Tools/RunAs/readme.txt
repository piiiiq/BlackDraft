使用 lsrunase，lsrunas 的加强版本，可以使用加密的密码。自带一个小软件 LSencrypt 用来生成加密的字串。
用法：
lsrunase /user:administrator /password:41BngA== /domain: /command:notepad.exe /runpath:c:\
所有的参数必须齐全，其中：
user 为运行的账号
password 为密码加密后的字串
domain 为机器名或域名，也可留空代表本机
command 为要运行的程序名，如果携带参数需要在命令的首尾加引号
runpath 为程序启动的路径
特点：可以较完美的替代 runas，并避免直接将密码明文保存在脚本中。
