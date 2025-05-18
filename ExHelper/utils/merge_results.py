def merge_results(results_list):
    """
    合并不同检索器的结果
    :param results_list: 不同检索器的结果列表
    :return: 合并后的结果列表
    """
    merged_results = []
    for results in results_list:
        for result in results:
            if result not in merged_results:
                merged_results.append(result)
    return merged_results