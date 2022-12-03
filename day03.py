from pathlib import Path


def item_score(item: str):
    if item.isupper():
        return ord(item) - ord('A') + 27
    else:
        return ord(item) - ord('a') + 1


def part1(backpacks: list[str]) -> int:
    score = 0
    for items in backpacks:
        left, right = items[len(items) // 2:], items[:len(items) // 2]
        common_items = list(set(left) & set(right))
        assert len(common_items) == 1
        score += item_score(common_items[0])
    return score


def form_groups(lines: list[str], group_size=3) -> list[str]:
    for i in range(0, len(lines), group_size):
        yield lines[i:i + group_size]


def part2(backpacks: list[str]) -> int:
    score = 0
    for items1, items2, items3 in form_groups(backpacks, 3):
        common_items = list(set(items1) & set(items2) & set(items3))
        assert len(common_items) == 1
        score += item_score(common_items[0])
    return score


if __name__ == '__main__':
    data = Path('inputs/day03.txt').read_text(encoding='utf-8')
    split_data = [line for line in data.splitlines()]
    answer1 = part1(split_data)
    print('Answer1:', answer1)  # 7872
    answer2 = part2(split_data)
    print('Answer2:', answer2)  # 2497
