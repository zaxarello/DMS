import pandas as pd
import sys


def transport_task(df):
    # Создаем списки запасов и требований узлов
    stocks = list(df["Запасы"])
    stocks.pop()
    num_of_needs = len(df["Запасы"]) - 1
    needs = []
    reference_plan = df.copy(deep=True)
    for i in range(1, len(df)):
        needs.append(df["B" + str(i)][num_of_needs])
        for j in range(num_of_needs):
            reference_plan.loc[j, "B" + str(i)] = 0
    # Применяем метод северо-западного узла
    reference_plan_north = reference_plan.copy(deep=True)
    reference_plan_minmax = reference_plan.copy(deep=True)
    equations_plan_north = []
    equations_plan_minmax = []
    min_north = northwest_corner(reference_plan_north, equations_plan_north, df, stocks.copy(), needs.copy())
    min_min_max = min_max(reference_plan_minmax, equations_plan_minmax, df, stocks.copy(), needs.copy())
    print("Опорный план северо-западного узла")
    print(reference_plan_north)
    print("F(северо-западный узел) =", min_north)
    print()
    print("Опорный план минимальной стоимости")
    print(reference_plan_minmax)
    print("F(минимальная стоимость) =", min_min_max)
    print()
    writer = pd.ExcelWriter("Result.xlsx", engine='xlsxwriter')
    if len(equations_plan_minmax) != (len(needs) + len(stocks) - 1):
        print("План минимальной стоимости является вырожденным, выбираем северо-западный узел")
        recurs_transport(df, reference_plan_north, equations_plan_north, writer, 0)
    else:
        if min_north <= min_min_max:
            print("План северо-западного узла является более оптимальным, выбираем его")
            recurs_transport(df, reference_plan_north, equations_plan_north, writer, 0)
        else:
            print("План минимальной стоимости является более оптимальным, выбираем его")
            recurs_transport(df, reference_plan_minmax, equations_plan_minmax, writer, 0)


def northwest_corner(reference_plan, equations, df, stocks, needs):
    sum = 0
    for i in range(len(stocks)):
        for j in range(len(needs)):
            if needs[j] != 0:
                equations.append([i, j, df.loc[i, "B" + str(j + 1)]])
                if stocks[i] < needs[j]:
                    reference_plan.loc[i, "B" + str(j + 1)] = stocks[i]
                    needs[j] = needs[j] - stocks[i]
                    stocks[i] = 0
                    sum += reference_plan.loc[i, "B" + str(j + 1)] * df.loc[i, "B" + str(j + 1)]
                    break
                else:
                    reference_plan.loc[i, "B" + str(j + 1)] = needs[j]
                    stocks[i] = stocks[i] - needs[j]
                    needs[j] = 0
                sum += reference_plan.loc[i, "B" + str(j + 1)] * df.loc[i, "B" + str(j + 1)]
    return sum


def min_max(reference_plan, equations, df, stocks, needs):
    null_needs = 0
    sum = 0
    while null_needs != len(needs):
        min_c = sys.maxsize
        cords = []
        for i in range(1, len(df)):
            for j in range(len(df["B" + str(i)]) - 1):
                if needs[i - 1] != 0 and stocks[j] != 0 and df.loc[j, "B" + str(i)] < min_c:
                    min_c = df.loc[j, "B" + str(i)]
                    cords.clear()
                    cords.append(j)
                    cords.append(i - 1)
        equations.append([cords[0], cords[1], df.loc[cords[0], "B" + str(cords[1] + 1)]])
        if stocks[cords[0]] < needs[cords[1]]:
            needs[cords[1]] = needs[cords[1]] - stocks[cords[0]]
            reference_plan.loc[cords[0], "B" + str(cords[1] + 1)] = stocks[cords[0]]
            stocks[cords[0]] = 0
        else:
            stocks[cords[0]] = stocks[cords[0]] - needs[cords[1]]
            reference_plan.loc[cords[0], "B" + str(cords[1] + 1)] = needs[cords[1]]
            needs[cords[1]] = 0
            null_needs += 1
        sum += reference_plan.loc[cords[0], "B" + str(cords[1] + 1)] * df.loc[cords[0], "B" + str(cords[1] + 1)]
    return sum


def recurs_transport(df, table, equations, writer, step):
    print("------------------------")
    table.to_excel(writer, startrow=step, index=False)
    min_v = solving_equations(equations.copy(), df, table)
    # Если нет отрицательных оценок, то завершаем выполнение
    if min_v[2] >= 0:
        print("Получено оптимальное решение!")
        df.to_excel(writer, startrow=(step + 10), index=False)
        writer.save()
        return
    # Получаем транспонированную матрицу значений
    matrix = []
    for i in range(1, len(table)):
        buf = list(table["B" + str(i)])
        buf.pop()
        matrix.append(buf)
    vertexes = precycle(matrix, min_v[1], min_v[0])
    min = sys.maxsize
    # Ищем минимальное значение среди вершин
    print()
    print(vertexes)
    for i in range(1, len(vertexes), 2):
        vertex = vertexes[i]
        num = table.loc[vertex[1], "B" + str(vertex[0] + 1)]
        if num < min and num != 0:
            min = num
    # Уменьшаем и увеличиваем вершины в цикле
    print("Минимальное значение:", min)
    sum = 0
    for vertex in vertexes:
        table.loc[vertex[1], "B" + str(vertex[0] + 1)] = table.loc[vertex[1], "B" + str(vertex[0] + 1)] + min
        sum += df.loc[vertex[1], "B" + str(vertex[0] + 1)] * table.loc[vertex[1], "B" + str(vertex[0] + 1)]
        # Удаляем нулевые уравнений
        if table.loc[vertex[1], "B" + str(vertex[0] + 1)] == 0:
            for equation in equations:
                if equation[0] == vertex[1] and equation[1] == vertex[0]:
                    equations.remove(equation)
        min = 0 - min
    # Добавляем новое уравнение
    equations.append([min_v[0], min_v[1], df.loc[min_v[0], "B" + str(min_v[1] + 1)]])
    print()
    print(table)
    print()
    print("F =", sum)
    print()
    recurs_transport(df, table, equations, writer, step + 10)


def precycle(matrix, y, x):
    cords = [[y, x]]
    # Проверяем горизонтальное и вертикальное направления
    if not cycle(matrix, cords, True):
        cycle(matrix, cords, False)
    return cords


def cycle(matrix, cords, vertical):
    final_cord = cords[0]
    cord = cords[len(cords) - 1]
    if vertical:
        x = cord[1]
        # Проверяем на возвращение в исходную точку
        if len(cords) != 1 and x == final_cord[1]:
            return True
        # Собираем y, которые уже были с этим x
        ys = []
        for old_cord in cords:
            if old_cord[1] == x:
                ys.append(old_cord[0])
        for y in range(len(matrix)):
            # Ищем новые вершины
            if not ys.__contains__(y) and matrix[y][x] != 0:
                cords.append([y, x])
                if cycle(matrix, cords, False):
                    return True
                cords.pop()
    else:
        y = cord[0]
        # Проверяем на возвращение в исходную точку
        if len(cords) != 1 and y == final_cord[0]:
            return True
        # Собираем x, которые уже были с этим y
        xs = []
        for old_cord in cords:
            if old_cord[0] == y:
                xs.append(old_cord[1])
        for x in range(len(matrix[0])):
            # Ищем новые вершины
            if not xs.__contains__(x) and matrix[y][x] != 0:
                cords.append([y, x])
                if cycle(matrix, cords, True):
                    return True
                cords.pop()
    return False


def solving_equations(equations, df, table):
    # Решаем систему уравнений
    min = sys.maxsize
    res = []
    u = [sys.maxsize for i in range(len(df) - 1)]
    y = [sys.maxsize for i in range(len(df["B1"]) - 1)]
    u[equations[0][0]] = 0
    print("U" + str(equations[0][0]), "=", 0)
    while len(equations) != 0:
        for equation in equations:
            if u[equation[0]] != sys.maxsize:
                print("U" + str(equation[0]), "+", "Y" + str(equation[1]), "=", equation[2])
                new_y = equation[2] - u[equation[0]]
                print("Y" + str(equation[1]), "=", new_y)
                y[equation[1]] = new_y
                equations.remove(equation)
            else:
                if y[equation[1]] != sys.maxsize:
                    print("U" + str(equation[0]), "+", "Y" + str(equation[1]), "=", equation[2])
                    new_u = equation[2] - y[equation[1]]
                    print("U" + str(equation[0]), "=", new_u)
                    u[equation[0]] = new_u
                    equations.remove(equation)
    # Высчитываем относительные оценки
    for i in range(1, len(df)):
        for j in range(len(df["B"+str(i)]) - 1):
            if table.loc[j, "B"+str(i)] == 0:
                v = df.loc[j, "B" + str(i)] - u[j] - y[i - 1]
                print("V" + str(j) + str(i - 1), " = ", v)
                if v < min:
                    min = v
                    res.clear()
                    res = [j, i-1]
    print("min:", min)
    res.append(min)
    return res


xl = pd.ExcelFile("Start.xls")
transport_task(xl.parse(xl.sheet_names[0]))
