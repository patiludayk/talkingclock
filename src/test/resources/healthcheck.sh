#!/bin/bash

# User-configurable variables
SOURCE_BROKER="<source-cluster-broker>:9092"      # Source Kafka cluster broker
TARGET_BROKER="<target-cluster-broker>:9092"      # Target Kafka cluster broker
MIRRORMAKER_GROUP="<mirrormaker-consumer-group>"  # Consumer group used by MirrorMaker
LAG_THRESHOLD=1000                                # Max acceptable lag per partition

# Path to Kafka tools (adjust based on your Kafka setup)
KAFKA_HOME="/path/to/kafka" # Replace with your Kafka installation directory
KAFKA_CONSUMER_GROUP_TOOL="$KAFKA_HOME/bin/kafka-consumer-groups.sh"

# Check Kafka broker connectivity
function check_broker_connectivity {
  local broker=$1
  local host=$(echo "$broker" | cut -d':' -f1)
  local port=$(echo "$broker" | cut -d':' -f2)

  echo "Checking connectivity to broker: $broker"
  if nc -zv "$host" "$port"; then
    echo "Broker $broker is reachable"
    return 0
  else
    echo "Error: Broker $broker is not reachable"
    return 1
  fi
}

# Check Source Broker Connectivity
if ! check_broker_connectivity "$SOURCE_BROKER"; then
  exit 1
fi

# Check Target Broker Connectivity
if ! check_broker_connectivity "$TARGET_BROKER"; then
  exit 1
fi

# Check if Kafka consumer groups tool exists
if [ ! -f "$KAFKA_CONSUMER_GROUP_TOOL" ]; then
  echo "Error: Kafka consumer group tool not found at $KAFKA_CONSUMER_GROUP_TOOL"
  exit 1
fi

# Fetch consumer group details
OUTPUT=$($KAFKA_CONSUMER_GROUP_TOOL --bootstrap-server "$SOURCE_BROKER" \
                                    --group "$MIRRORMAKER_GROUP" \
                                    --describe 2>&1)

if [[ $? -ne 0 ]]; then
  echo "Error: Failed to fetch consumer group details"
  echo "$OUTPUT"
  exit 1
fi

# Parse and analyze consumer group lag
echo "Checking consumer group lag for group: $MIRRORMAKER_GROUP"
LAG_EXCEEDED=false

while read -r line; do
  # Ignore headers or unrelated lines
  if [[ "$line" == *"TOPIC"* || "$line" == *"PARTITION"* ]]; then
    continue
  fi

  # Extract topic, partition, current offset, log end offset, and lag
  TOPIC=$(echo "$line" | awk '{print $1}')
  PARTITION=$(echo "$line" | awk '{print $2}')
  LAG=$(echo "$line" | awk '{print $5}')

  # Check if lag is numeric and exceeds threshold
  if [[ "$LAG" =~ ^[0-9]+$ && "$LAG" -gt "$LAG_THRESHOLD" ]]; then
    echo "Warning: Lag on $TOPIC partition $PARTITION is $LAG (exceeds $LAG_THRESHOLD)"
    LAG_EXCEEDED=true
  fi
done <<< "$OUTPUT"

# Final health decision
if $LAG_EXCEEDED; then
  echo "Error: Lag threshold exceeded for one or more partitions"
  exit 1
else
  echo "Success: Consumer lag is within acceptable thresholds"
  exit 0
fi
