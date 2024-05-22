INSERT INTO collection (name,
                        coordinatesX,
                        coordinatesY,
                        creationDate,
                        health,
                        category,
                        weaponType,
                        meleeWeapon,
                        chapterName,
                        chapterParentLegion,
                        owner)
VALUES (?, ?, ?, ?, ?, CAST(? AS ASTARTESCATEGORY), CAST(? AS WEAPONTYPE), CAST(? AS MELEEWEAPON), ?, ?, ?) RETURNING id;