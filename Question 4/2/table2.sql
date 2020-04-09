 COPY (SELECT M.moveName AS effective_move, M.DPS, M.typename AS type_of_effective_move, T.typename AS type_being_attacked
 FROM Moves M, Types T
 WHERE T.typename='Fire' AND M.typename = T.vulnerableTo
 ORDER BY M.DPS) TO 'C:\Users\User\Desktop\Lectures Winter 2020\COMP 421\Projects\P3\table2.csv' WITH CSV;
