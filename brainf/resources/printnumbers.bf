++++++++++                                              new line            '\n'
>++++++++++++++++++++++++++++++++++++++++++++++++       second digit        '0'   
>++++++++++++++++++++++++++++++++++++++++++++++++       first digit         '0'
>++++++++++                                             inner loop counter  10
>++++++++++                                             outer loop counter  10
[   outer loop
   <                                                       back to inner loop counter
   [  inner loop
      <.<.<.      print number
      >+          increment second digit by one
      >>-         decrement inner loop counter by one
   ]
   ++++++++++   reset inner loop counter to 10
   <+           increment first digit by one
   <----------  second digit to '0'
   >>>-         decrement outer loop counter by one
]
