import pandas as pd
import os
import numpy as np

from pandas import DataFrame


def mai(path_to_dir, file, fileResult):
    os.chdir(path_to_dir)
    xl = pd.ExcelFile(file)
    df1 = xl.parse(xl.sheet_names[0])
    criteria_num = dict()
    for i in range(1, len(df1) + 1):
        criteria_num[list(df1)[i].split("(")[0]] = (int((list(df1)[i].split("(")[1].split(")")[0])))
    criteria_table = pairwise_comparison(criteria_num, 9)
    criteria_matrix = np.array(compute_w(criteria_table, "Критерии"))
    writer = pd.ExcelWriter(fileResult, engine='xlsxwriter')
    DataFrame(criteria_table).to_excel(writer, index=False)
    start_row = len(df1) + 2
    alternative_matrix_buffer = []
    for crit in range(1, len(df1) + 1):
        alternative_num = dict()
        for alternative in range(len(df1[list(df1)[0]])):
            alternative_num[df1[list(df1)[0]][alternative]] = df1[list(df1)[crit]][alternative]
        alternative_table = pairwise_comparison(alternative_num, 9, list(df1)[crit])
        DataFrame(alternative_table).to_excel(writer, startrow=start_row,
                                              index=False)
        alternative_matrix_buffer.append(compute_w(alternative_table, list(df1)[crit]))
        start_row += len(df1[list(df1)[0]]) + 2
    writer.save()
    alternative_matrix = np.array(alternative_matrix_buffer).transpose()
    result = alternative_matrix.dot(criteria_matrix)
    max = -1
    best = ''
    print("РЕЗУЛЬТАТ:")
    for j in range(len(result)):
        num = result[j]
        if num > max:
            max = num
            best = df1[list(df1)[0]][j]
        print(df1[list(df1)[0]][j], ": ", num)
    print("ПОБЕДИТЕЛЬ: ", best, " со счетом ", max)


def compute_w(dic, name="-"):
    buf_res = []
    sum_of_sum = 0
    for i in range(len(dic[list(dic)[0]])):
        sum = 0
        for j in range(1, len(dic)):
            sum += dic[list(dic)[j]][i]
        buf_res.append(sum)
        sum_of_sum += sum
    res = []
    for i in range(len(buf_res)):
        buf = buf_res[i] / sum_of_sum
        print(name, " W", i, ": ", buf)
        res.append(buf_res[i] / sum_of_sum)
    print("-------------")
    return res


def pairwise_comparison(dict2, normalization, name1="-"):
    res = {name1: []}
    max_pass = max(dict2.values()) - min(dict2.values())
    for name in dict2:
        res[name1].append(name)
        res[name] = []
        for i in list(dict2):
            res[name].append(0)
    for i in range(len(dict2)):
        for j in range(i, len(dict2)):
            num = round(abs(list(dict2.values())[i] - list(dict2.values())[j]) / max_pass * (normalization - 1)) + 1
            if list(dict2.values())[i] > list(dict2.values())[j]:
                res[list(res)[j + 1]][i] = num
                res[list(res)[i + 1]][j] = 1 / num
            else:
                res[list(res)[i + 1]][j] = num
                res[list(res)[j + 1]][i] = 1 / num
    return res


mai("C:\\Users\\zaxar\\OneDrive\\Документы\\ВУЗ\\Теория принятия решений\\МАИ", "mai.xls", "res.xlsx")
