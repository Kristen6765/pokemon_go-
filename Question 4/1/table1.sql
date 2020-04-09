COPY (SELECT P.code AS "Group code", COUNT(P.username)AS "Group size", ROUND(AVG(P.level),2)AS "Average level"
 FROM Players P
 GROUP BY P.code
 HAVING COUNT(P.username)>1
 ORDER BY P.code) TO 'C:\Users\User\Desktop\Lectures Winter 2020\COMP 421\Projects\P3\table1.csv' WITH CSV;