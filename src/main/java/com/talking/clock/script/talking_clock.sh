#!/bin/sh

digit2words() {
    local -i num=$((10#$1))
    if ((num < 20)); then
        echo "${digits[num]}"
    elif ((num == 20)); then
        echo "twenty"
    else
        echo "twenty" "${digits[num % 10]}"
    fi
}

if [ $# -eq 0 ]
  then
	declare time=(`date | cut -d " " -f5 | cut -d ":" -f1-2`)

	#echo $time

	declare -i hour=(`date | cut -d " " -f5 | cut -d ":" -f1`)
	declare -i minute=(`date | cut -d " " -f5 | cut -d ":" -f2`)

	#echo $hour
	#echo $minute

	declare -i talking_hour=$hour
	declare -i talking_minute=$minute
	declare talk="past "
	declare fixed=""

	if ((minute > 30))
		then 
			talking_hour+=1
			talking_minute=60-$talking_minute
			talk="to "
	elif ((minute == 30))
		then
		 	talking_minute=
		 	talk="Half past "
	elif((minute == 0))
		then 
			talking_minute=
			talk=
			fixed="O'clock"
	fi

	if((talking_hour==0 || talking_hour==12 || talking_hour == 24)); then
		talking_hour=12
	elif((talking_hour > 12)); then
		talking_hour=$talking_hour-12
	fi

	#echo $talking_hour
	#echo $talking_minute
	#echo $talk

	declare digits=("" one two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen)

	declare min_to_word=$(digit2words $talking_minute)
	#echo $min_to_word
	declare hour_to_word=$(digit2words $talking_hour)
	#echo $hour_to_word


	echo $min_to_word $talk $hour_to_word $fixed
else
	declare time="$@"
	#echo $time

	declare -i hour=${time%:*}
	declare -i minute=${time#*:}
	
	#echo $hour
	#echo $minute

	declare -i talking_hour=$hour
	declare -i talking_minute=$minute
	declare talk="past "
	declare fixed=""

	if ((minute > 30))
		then 
			talking_hour+=1
			talking_minute=60-$talking_minute
			talk="to "
	elif ((minute == 30))
		then
		 	talking_minute=
		 	talk="Half past "
	elif((minute == 0))
		then 
			talking_minute=
			talk=
			fixed="O'clock"
	fi

	if((talking_hour==0 || talking_hour==12 || talking_hour == 24)); then
		talking_hour=12
	elif((talking_hour > 12)); then
		talking_hour=$talking_hour-12
	fi

	#echo $talking_hour
	#echo $talking_minute
	#echo $talk

	declare digits=("" one two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen)

	declare min_to_word=$(digit2words $talking_minute)
	#echo $min_to_word
	declare hour_to_word=$(digit2words $talking_hour)
	#echo $hour_to_word


	echo $min_to_word $talk $hour_to_word $fixed

fi
