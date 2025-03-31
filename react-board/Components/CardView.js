import React from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity } from 'react-native'
import { useNavigation, navigate } from '@react-navigation/native';

export default function CardView({value}) {
  const navigation = useNavigation();

  console.log('------- cardView -----------')
  console.log('value -> \n', value)
    
  return (
    <TouchableOpacity style={cardStyle.container} 
      onPress={()=> { navigation.navigate('DetailPage', { detailTip: value }); }}>
      <Image source={{uri:value.image}} 
              style={{width: 100, height: 80, margin: 2, borderRadius: 10}}></Image>
      <View style={cardStyle.textPost}>
      <Text style={cardStyle.textTitle}>{value.title}</Text>
      <Text style={cardStyle.textDesc} numberOfLines={3}>{value.desc}</Text>
      <Text style={cardStyle.textDate}>{value.date}</Text>
      </View>
    </TouchableOpacity>
  )
}

const cardStyle = StyleSheet.create({
      container: {
        flexDirection: 'row',
        width: '100%',
      },
      textPost: {
        flexDirection: 'column',
        paddingHorizontal: 3
      },
      textTitle: {
        fontSize: 17,
        fontWeight: 700
      },
      textDesc: {
        fontsize: 14,
        paddingRight: 3,
      },
      textDate: {
        fontSize: 12,
        color: '#d3d3d3'
      }
})