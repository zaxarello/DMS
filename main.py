import pandas as pd
import xlsxwriter


def simplex_recurs(df):
    # Вычисляем разрешающий столбец
    print(df)
    print()
    min = 0
    column = ""
    for i in range(1, len(df) - 2):
        key = list(df)[i]
        mass = df[key]
        if mass[len(mass) - 1] < min:
            min = mass[len(mass) - 1]
            column = key
    print("Число", min, "является минимальной отрицательной относительной оценкой. Находится в столбце", column)
    print()
    # Вычисляем разрешающую строку
    massA = df["A"]
    massColumn = df[column]
    raw = 0
    min = -1
    for i in range(len(massA) - 1):
        if float(massColumn[i]) <= 0:
            continue
        num = float(massA[i]) / float(massColumn[i])
        if min == -1 or num < min:
            min = num
            raw = i
    print("Отношение элемента А столбца к элементу разрешающего столбца равное", min, "является минимальным "
                                                                                      "положительным отношением. "
                                                                                      "Следовательно строка под "
                                                                                      "номером", raw + 1,
          "является разрешающей строкой.")
    print()
    if min == -1:
        raise Exception("Целевая функция не ограничена")
    resolution_element = df[column][raw]
    print("Число", resolution_element, "является разрешающим элементом")
    print()
    # column - ключ разрещающего столбца, raw - номер разрещающей строки, resolution_element - разрещающий элемент
    # Формируем новую таблицу
    newDf = dict()
    # Заолняем первый столбец
    newDf["C"] = []
    for i in range(len(massA) - 1):
        if i == raw:
            newDf["C"].append(column)
        else:
            newDf["C"].append(df["C"][i])
    newDf["C"].append("F")
    final = True
    # Заполняем остальные строки
    for key in df:
        if key == "C":
            continue
        # Отдельно заполняем разрещающий столбец
        if key == column:
            newDf[df["C"][raw]] = []
            for i in range(len(df[key])):
                # Отдельно заполняем разрещающий элемент
                if i == raw:
                    print("Для столбца", key, "и строки под номером", i + 1,
                          "вычисляем новый элемент через деление разрешающего элемента на единицу. 1 /",
                          df[key][i], " =", 1 / df[key][i])
                    print()
                    newDf[df["C"][raw]].append(1 / df[key][i])
                else:
                    print("Для столбца", key, "и строки под номером", i + 1,
                          "вычисляем новый элемент через деление элемента на разрешающий элемент и изменение знака. 0 -",
                          df[key][i], " /", resolution_element, " =", 0 - df[key][i] / resolution_element)
                    print()
                    newDf[df["C"][raw]].append(0 - df[key][i] / resolution_element)
                if i == len(df[key]) - 1 and newDf[df["C"][raw]][i] < 0:
                    final = False
        else:
            newDf[key] = []
            for i in range(len(df[key])):
                # Отдельно заполняем разрещающую строку
                if i == raw:
                    print("Для столбца", key, "и строки под номером", i + 1,
                          "вычисляем новый элемент через деление элемента на разрешающий элемент.",
                          df[key][i], " /", resolution_element, " =", df[key][i] / resolution_element)
                    print()
                    newDf[key].append(df[key][i] / resolution_element)
                # Заполняем все остальные элементы через прямоугольник
                else:
                    print("Для столбца", key, "и строки под номером", i + 1,
                          "вычисляем новый элемент через правило прямоугольника. Получаем ",
                          (df[key][i] * resolution_element - df[column][i] * df[key][raw]) / resolution_element)
                    print()
                    newDf[key].append(
                        (df[key][i] * resolution_element - df[column][i] * df[key][raw]) / resolution_element)
                if i == len(df[key]) - 1 and key != "A" and newDf[key][i] < 0:
                    final = False
    res = [pd.DataFrame(newDf)]
    if final:
        return res
    # Если остались отрицательные дельты, то продолжаем выполнение
    print()
    print()
    print("------------------------------------------")
    print()
    print()
    res.extend(simplex_recurs(pd.DataFrame(newDf)))
    return res


def simplex(fileStart, fileResult):
    xl = pd.ExcelFile(fileStart)
    df1 = xl.parse(xl.sheet_names[0])
    res = simplex_recurs(df1)
    writer = pd.ExcelWriter(fileResult, engine='xlsxwriter')
    df1.to_excel(writer, index=False)
    start_row = len(df1) + 2
    for df in res:
        df.to_excel(writer, startrow=start_row,
                    index=False)
        start_row += len(df) + 2
    writer.save()


simplex("Starting.xls", "Result.xlsx")
