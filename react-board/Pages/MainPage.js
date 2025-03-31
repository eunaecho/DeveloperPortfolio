import CardView from '../Components/CardView';
import { StyleSheet, Text, View, Image, ScrollView, Dimensions, TouchableOpacity, SafeAreaView} from 'react-native';
import { useEffect, useState } from 'react';
import LoadingPage from './LoadingPage';
import { db } from '../firebaseConfig';

const mainImg = 'https://storage.googleapis.com/sparta-image.appspot.com/lecture/main.png';
const category = ['전체','일상생활', '반려견', '재테크'];

export default function MainPage() {
    const [dataSet, isDataSet] = useState(false)
    const [data, setData] = useState([]);
    const [cate, setCate] = useState('전체')
    
    /***** useEffect() *****/
    useEffect(()=>{
        db.ref('/tip').on('value', (snapshot) => {
          if(snapshot.exists()) {
            setData(snapshot.val())
            isDataSet(true);
            console.log('reDataSet')
          } else {
            console.log('no tips!');
            return null;
          }
        });
    },[])
    
    return(
      isDataSet?
      <View style={styles.safeAreaContainer}>
        <ScrollView style={styles.scrollViewContainer}>
            <View style={styles.containerOne}>
              <Image source={{uri:mainImg}}
                    style={{width: Dimensions.get('screen').width-16, height:200, borderRadius:18}}></Image>
            </View>
      
            <View style={styles.containerTwo}>
              <ScrollView horizontal={true}>
                {
                  category.map((value, i) => {
                    return(
                      <TouchableOpacity style={styles.scrollBtn} key={i}>
                        <Text style={styles.scrollBtnText} onPress={()=> (setCate(value))}>{value}</Text>
                      </TouchableOpacity>
                    )
                  })
                }
              </ScrollView>       
      
              <View style={styles.postBoard}>
                { (cate=='전체') ?
                  data.map((value, i)=> {
                    return(
                      <CardView value={value} key={i}/>
                    )
                  })
                : data.filter((content => content.category == cate )).map((value, i)=> {
                  return(
                    <CardView value={value} key={i}/>
                  )
                  }) 
                }
              </View>
            </View>
          </ScrollView>
        </View>
    : <LoadingPage/>
    )

}

const styles = StyleSheet.create({
    safeAreaContainer: {
      flex: 1,
    },
    scrollViewContainer: {
      backgroundColor: '#fff'
    },
    category: {
      fontWeight: 500,
      paddingVertical: 5,
      fontSize: 20,
    },
    containerOne: {
      flex: 1,
      alignItems: 'flex-start',
      padding: 8
    },
    containerTwo: {
      flex: 4,
      flexDirection: 'column',
      paddingHorizontal: 5,
    }, 
    horizonalScroll: {
      flex: 1,
      flexDirection: 'row'
    },
    scrollBtn: {
      flex:1,
      flexDirection: 'column',
      justifyContent: 'center',
      width: 100,
      height: 50,
      borderRadius: 12,
      backgroundColor:'#00a86b',
      marginHorizontal: 2,
    },
    scrollBtnText: {
      color: '#fff',
      textAlign: 'center',
    },
    postBoard: {
      flex: 5,
      flexDirection: 'column',
      paddingHorizontal: 2,
      paddingTop: 5
    },
  });