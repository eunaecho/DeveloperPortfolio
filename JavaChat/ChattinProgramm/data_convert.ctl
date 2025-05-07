LOAD DATA
INFILE '/Users/eunae/Library/Mobile Documents/com~apple~CloudDocs/eclipse-workspace/eunae-workspace/ChattinProgramm/zipcode.csv'
INTO TABLE search_post
FIELDS TERMINATED BY ','
IGNORE 1 LINES
(idx, zipcode, sido, gugun, dong, ri, bldg, bungi)
