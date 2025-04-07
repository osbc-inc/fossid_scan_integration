
Prerequisite
------------------------------------------------
### zip install
- This tool needs a 'zip' utility in your system.
  + Note: Install or use v3.0 or higher to create over 2GB zip file  
  + Windows: 
    - 'zip.exe' downoald url: http://stahlworks.com/dev/index.php?tool=zipunzip
    - Need to set path of `zip.exe` in your system environment    
  + Linux:
 ```bash
 $ yum install zip
 or
 $ apt-get install zip
 ``` 

### FossID
- Create FossID Username
- Create Api Key of Username
- Edit the /fossid/etc/fossid.conf file
```
;Enable API usage of target paths
webapp_scan_path_enable=1
```
- Add timeout and client_max_body_size in /etc/nginx/nginx.conf if Nginx runs as webserver

```
http {
    client_max_body_size 10G;
    proxy_connect_timeout 1200s;
    proxy_send_timeout 1200s;
    proxy_read_timeout 1200s;
    fastcgi_send_timeout 1200s;
    fastcgi_read_timeout 1200s;
}
```
- Edit below parameter in /etc/php.ini

```
max_execution_time = 300
post_max_size = 10000M
upload_max_filesize = 10000M
```

### Java JDK
- This tool is tested with Open JDK 1.8


Usage
------------------------------------------------

### To run this tool
```bash
$ java -jar class [args....]

e.g)
$ java -jar fossid_scan_integration.jar --protocol http --address fossid.co.kr/webapp --username unsername --apikey a22d2s2s23 --projectname testProject --projectcode 0000 --scanname testScan --scancode 0000 --targetpath /path/to/scan --dependencyscanrun 0 --gitrepourl https://github.com/twbs/bootstrap.git --gitbranch master --sourcepath /fossid/uploads/files/scans --ignorevalue licenses,lib --ignoretype directory,directory --interval 30 --filepath /path/to/scan --filename filename.zip --decompresstime 30 --excludepath /exclude/path1/*,/exclude/path2/*,*.txt
```

### Arguments
```
Server Information
--protocol: (Optional) FossID web interface protocol  
            (default: http)
--address: FossID address
--username: username
--apikey: apikey

Project/Scan Information  
--projectname: Project Name
(Optional) --projectcode: Project Code
--scanname: Scan Name
(Optional) --scancode: Scan Code

Option (Optional)  
--targetpath: Full path including source code to be analyzed in FossID server  
  + Need to change 'webapp_scan_path_enable=1' to use this option in /fossid/etc/fossid.conf file  
  (NOTE: Please, do not use `--targetpath` with `Git Config` and `Upload Target File`)
--dependencyscanrun: Set 0 or 1 to trigger dependency scan after source code scan (default: 0)
--ignorevalue: Set ignore values. This value is separated by commas and the order of this values is matched with the order of ignoretypes value  
--ignoretype: Set ignore types. This value is separated by commas and the order of this values is matched with the order of ignorevalue value  
--interval: Set interval time to check status running source code and dependency scan (default: 10 seconds)
--debug: Pring debug message of scan process (default: 0, 1)

Git Config (Optional)
(NOTE: Please, do not use `Git Config` with `--targetpath` and `Upload Target File`)
(NOTE: Applying `--gitrepourl` and `--gitbranch` can be applied when creating a new scan)
--gitrepourl: Set git repo url 
--gitbranch: Set git repo branch
--gitrepourlchange: Set '1' if git repo url is changed (0 default,1)
--gitbranchchange: Set '1' if git repo branch is changed (0 default,1)

Upload Local Target File (Optional)
(NOTE: Please, do not use `Upload Target File` with `--targetpath` and `Git Config`)
--filepath: full file path with file name to be analyzed. The souce code must be archived before starting this tool
--filename: compressed file name
--decompresstime: Set the seconds to decompress the compressed file (default: 30 seconds)

Scan Option (Optional)
--limit: Limit on number of FOSSID results (Default/Recommended: 10)
--sensitivity: Sensitivity of the scan (Default/Recommended: 10. A value lower than 6 will return full file matches only)
--replaceid: Replace existing identifications (0 default,1)
--reuseid: If exists, try to use an existing identification depending on parameter 'identification_reuse_type' (0 default,1)
--idreusetype: Last identification found will be used for files with the same hash (any, only_me, specific_project, specific_scan)
--specificcode: Only when 'identification_reuse_type' is 'specific_project' or 'specific_scan'
--autoiddetectdeclare: Automatically detect license declaration inside files (0 default,1)
--autoiddetectcopyright: Automatically detect copyright statements inside files (0 default,1)
--autoidresolvependingids: Automatically resolve pending identifications (0 default,1)
--scanfailedonly: If true, this will only scan files that have failed in the previous scan (0 default,1)
--deltaonly: Only newly added files or modified files will be scanned (0,1 default)
--full_file_only: Will result in only full file matches being returned regardless of sensitivity setting (0 default,1)
--idassist_ams: If true, scan will run with advanced match scoring. When no value is provided, the default behaviour is enabled (0,1 default)  
--idassist_mft: Minimum length (in characters) of snippet to be considered valid after applying intelligent match filtering. Set to 0 to disable intelligent match filtering for current scan. If parameter is not sent it defaults to value of ‘webapp_match_filtering_threshold‘ from fossid.conf. If there is no value in fossid.conf the default is 300 characters (0,1 default)  
```