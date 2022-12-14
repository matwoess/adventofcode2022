import itertools
from pathlib import Path
import ast
from functools import cmp_to_key


def parse_package_pair(pair_str: str) -> tuple[list, list]:
    packet1, packet2 = pair_str.split('\n')
    return ast.literal_eval(packet1), ast.literal_eval(packet2)


def compare(value1, value2) -> int:
    for left, right in itertools.zip_longest(value1, value2):
        match (left, right):
            case None, _:
                return -1
            case _, None:
                return 1
            case int(), int():
                if left != right:
                    return left - right
            case _, _:
                if isinstance(left, int):
                    left = [left]
                if isinstance(right, int):
                    right = [right]
                if (res := compare(left, right)) != 0:
                    return res
    return 0


def part1(packet_pairs: list[tuple[list, list]]) -> int:
    results = [compare(*pair) for pair in packet_pairs]
    indices_right_order = [i + 1 for i, res in enumerate(results) if res < 0]
    return sum(indices_right_order)


def part2(data: list[tuple[list, list]]) -> int:
    divider_packet2 = [[2]]
    divider_packet6 = [[6]]
    all_packets = list(sum(data, ()))
    all_packets.extend([divider_packet2, divider_packet6])
    all_packets.sort(key=cmp_to_key(compare))
    d2_index = all_packets.index(divider_packet2) + 1
    d6_index = all_packets.index(divider_packet6) + 1
    return d2_index * d6_index


if __name__ == '__main__':
    raw_input = Path('inputs/day13.txt').read_text(encoding='utf-8')
    package_pairs = [parse_package_pair(pair_str) for pair_str in raw_input.split('\n\n')]
    answer1 = part1(package_pairs)
    print('Answer1:', answer1)  # 6187
    answer2 = part2(package_pairs)
    print('Answer2:', answer2)  # 23520
