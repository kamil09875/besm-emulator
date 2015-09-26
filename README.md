# BESM Emulator
Extended emulator of BESM - Soviet mainframe computer, implemented in Java.

# Usage

Type `besm -h` for help.

 - `besm <fielname>` - read and process `filename`,
 - `besm <filename> -b` - work in binary mode,
 - `besm <filename> -d` - work in debug mode,
 - `besm <filename> -e` - work in extended debug mode.

## File format

Every file contains memory cells. A single cell consists of address and value.

### Binary mode

In binary mode file is read as an array of 32-bit integer values. Every value
has it's corresponding address starting from `0` and incrementing till `array_length - 1`.

Example:

```
01050607 // first instruction - c = a + b
00010007 // second instruction - print c
00000000 // third instruction - stop

00000000
00000000

0000A012 // a
00000701 // b
00000000 // c
```

### Plain mode

In plain mode input is encoded as text. Every cell has it's given address.

Example:

```
0: 1 5 6 7 // zero address value - c = a + b
1: 0 1 0 7 // print c
2: 0 0 0 0 // stop

5: 147 // a
6: 23  // b
7: 0   // c
```
