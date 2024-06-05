AnchorPane
AnchorPane позволяет фиксировать одну или несколько сторон дочерних элементов к определенной позиции на панели. Это может быть полезно, если вам нужно, чтобы компоненты оставались на месте при изменении размеров сцены.


BorderPane
BorderPane разделяет пространство на пять областей: top, bottom, left, right и center. Каждая область может содержать только один узел (node), и все из них выбираются автоматически для заполнения доступного пространства.


FlowPane
FlowPane располагает дочерние элементы подряд, перенося их на новую строку, если для них не хватает места по горизонтали, подобно текстовому редактору.


GridPane
GridPane позволяет расположить дочерние элементы в виде сетки с различными строками и столбцами, предоставляя большую гибкость в размещении компонентов.


HBox
HBox размещает дочерние элементы горизонтально в одну строку.


VBox
VBox размещает дочерние элементы вертикально в один столбец.


StackPane
StackPane позволяет располагать дочерние элементы в стопку, один поверх другого.


TilePane
TilePane похож на FlowPane, но вместо того чтобы допускать элементы разного размера, он выравнивает их в равномерную сетку за счет обрезки или масштабирования содержимого.


Pane
Pane является базовым менеджером компоновки без специальных правил расположения, позволяя вам устанавливать расположение дочерних элементов абсолютно.


Group
Group не является менеджером компоновки в традиционном смысле, так как размеры и позиции дочерних элементов задаются явно и не меняются автоматически.
Каждый из этих менеджеров компоновки выполняет уникальную роль и выбирается в зависимости от требований вашего GUI. Иногда для достижения желаемой компоновки используют комбинации различных менеджеров компоновки.