from pathlib import Path
from dataclasses import dataclass


@dataclass
class Assignment:
    sections1: set
    sections2: set

    @classmethod
    def from_string(cls, string: str) -> 'Assignment':
        assignment1, assignment2 = string.split(",")
        sections1 = cls.get_section_range(assignment1)
        sections2 = cls.get_section_range(assignment2)
        return Assignment(set(sections1), set(sections2))

    @staticmethod
    def get_section_range(assignment: str) -> range:
        section_from, section_to = map(int, assignment.split('-'))
        return range(section_from, section_to + 1)


def part1(assignments: list[Assignment]) -> int:
    return sum(a.sections1 <= a.sections2 or a.sections2 <= a.sections1 for a in assignments)


def part2(assignments: list[Assignment]) -> int:
    return sum((a.sections1 & a.sections2) != set() for a in assignments)


if __name__ == '__main__':
    raw_data = Path('inputs/day04.txt').read_text(encoding='utf-8')
    data = [Assignment.from_string(line) for line in raw_data.splitlines()]
    answer1 = part1(data)
    print('Answer1:', answer1)  # 588
    answer2 = part2(data)
    print('Answer2:', answer2)  # 911
