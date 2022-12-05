from pathlib import Path
from collections import deque
import re
from dataclasses import dataclass


@dataclass
class Rearrangement:
    source: int
    destination: int
    amount: int

    pattern = re.compile(r'move (\d+) from (\d+) to (\d+)')

    @classmethod
    def from_string(cls, string: str) -> 'Rearrangement':
        n, s, d = cls.pattern.match(string).groups()
        return Rearrangement(int(s), int(d), int(n))


def pre_process(stack_def, rearrangement_def):
    rows = stack_def.split("\n")
    number_row = rows.pop()
    n_stacks = int(re.match(r'.*(\d+).*', number_row).groups()[-1])
    stacks = [deque() for _ in range(n_stacks)]
    for row in reversed(rows):
        # starting with 1, every 4th index: [1]3[5]7[9]...
        for stack, letter_idx in zip(stacks, range(1, 4 * n_stacks, 4)):
            char = row[letter_idx]
            if char != ' ':
                stack.append(char)
    rearrangements = [Rearrangement.from_string(r)
                      for r in rearrangement_def.split('\n')
                      if r.strip() != ""]
    return stacks, rearrangements


def part1(stacks: list[deque], rearrangements: list[Rearrangement]) -> str:
    for r in rearrangements:
        for _ in range(r.amount):
            stacks[r.destination - 1].append(stacks[r.source - 1].pop())
    return ''.join([s.pop() for s in stacks])


def part2(stacks: list[deque], rearrangements: list[Rearrangement]) -> str:
    for r in rearrangements:
        stacks[r.destination - 1] += reversed([stacks[r.source - 1].pop() for _ in range(r.amount)])
    return ''.join([s.pop() for s in stacks])


if __name__ == '__main__':
    data = Path('inputs/day05.txt').read_text(encoding='utf-8')
    stack_str, rearrangement_str = data.split("\n\n")
    stacks_list, rearrangement_list = pre_process(stack_str, rearrangement_str)
    answer1 = part1(stacks_list.copy(), rearrangement_list.copy())
    print('Answer1:', answer1)  # FZCMJCRHZ
    stacks_list, rearrangement_list = pre_process(stack_str, rearrangement_str)
    answer2 = part2(stacks_list, rearrangement_list)
    print('Answer2:', answer2)  # JSDHQMZGF
